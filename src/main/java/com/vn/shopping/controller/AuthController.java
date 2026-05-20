package com.vn.shopping.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.Role;
import com.vn.shopping.domain.response.ResCreateUserDTO;
import com.vn.shopping.domain.response.ResLoginDTO;
import com.vn.shopping.domain.response.ResLoginDTO.UserLogin;
import com.vn.shopping.domain.request.ReqChangePasswordDTO;
import com.vn.shopping.domain.request.ReqLoginDTO;
import com.vn.shopping.service.KhachHangService;
import com.vn.shopping.service.VerificationTokenService;
import com.vn.shopping.service.EmailService;
import com.vn.shopping.service.NhanVienService;
import com.vn.shopping.service.StorageService;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final Pattern VIETNAM_PHONE_PATTERN = Pattern.compile("^(0\\d{9}|\\+84\\d{9})$");

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final KhachHangService khachHangService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final NhanVienService nhanVienService;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    @Value("${shopping.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @Value("${shopping.frontend-login-url}")
    private String frontendLoginUrl;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            KhachHangService khachHangService,
            VerificationTokenService verificationTokenService,
            EmailService emailService,
            NhanVienService nhanVienService,
            StorageService storageService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.khachHangService = khachHangService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.nhanVienService = nhanVienService;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        // Kiểm tra NhanVien trước
        NhanVien nhanVien = this.nhanVienService.findByEmail(loginDto.getUsername());
        if (nhanVien != null) {
            // Trigger lazy loading để tránh proxy issues
            Role role = nhanVien.getRole();
            if (role != null) {
                role.getName(); // Trigger lazy loading
            }
            String cuaHang = nhanVien.getCuaHang().getTenCuaHang();
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    nhanVien.getId(),
                    nhanVien.getEmail(),
                    nhanVien.getTenNhanVien(),
                    nhanVien.getSoDienThoai(),
                    nhanVien.getAvatar(),
                    role,
                    null,
                    cuaHang);
            res.setUser(userLogin);
        } else {
            // Kiểm tra KhachHang
            KhachHang currentUserDB = this.khachHangService.findByEmail(loginDto.getUsername());
            if (currentUserDB != null) {
                // Trigger lazy loading để tránh proxy issues
                Role role = currentUserDB.getRole();
                if (role != null) {
                    role.getName(); // Trigger lazy loading
                }
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                        currentUserDB.getId(),
                        currentUserDB.getEmail(),
                        currentUserDB.getTenKhachHang(),
                        currentUserDB.getSdt(),
                        currentUserDB.getAvatar(),
                        role,
                        currentUserDB.getDiemTichLuy(),
                        null);
                res.setUser(userLogin);
            }
        }

        // create access token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);

        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDto.getUsername(), res);

        // update user
        this.nhanVienService.updateUserToken(refresh_token, loginDto.getUsername());
        this.khachHangService.updateUserToken(refresh_token, loginDto.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        NhanVien nhanVienDB = this.nhanVienService.findByEmail(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

        if (nhanVienDB != null) {
            // Trigger lazy loading để tránh proxy issues
            Role role = nhanVienDB.getRole();
            if (role != null) {
                role.getName(); // Trigger lazy loading
            }
            userLogin.setId(nhanVienDB.getId());
            userLogin.setEmail(nhanVienDB.getEmail());
            userLogin.setName(nhanVienDB.getTenNhanVien());
            userLogin.setSdt(nhanVienDB.getSoDienThoai());
            userLogin.setAvatar(nhanVienDB.getAvatar());
            userLogin.setRole(role);
            userLogin.setDiemTichLuy(null);
            userGetAccount.setUser(userLogin);
        } else {
            KhachHang currentUserDB = this.khachHangService.findByEmail(email);
            if (currentUserDB != null) {
                // Trigger lazy loading để tránh proxy issues
                Role role = currentUserDB.getRole();
                if (role != null) {
                    role.getName(); // Trigger lazy loading
                }
                userLogin.setId(currentUserDB.getId());
                userLogin.setEmail(currentUserDB.getEmail());
                userLogin.setName(currentUserDB.getTenKhachHang());
                userLogin.setSdt(currentUserDB.getSdt());
                userLogin.setAvatar(currentUserDB.getAvatar());
                userLogin.setRole(role);
                userLogin.setDiemTichLuy(currentUserDB.getDiemTichLuy());
                userGetAccount.setUser(userLogin);
            }
        }

        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token)
            throws IdInvalidException {

        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        // check user by token + email (NhanVien hoặc KhachHang)
        NhanVien nhanVien = this.nhanVienService.getUserByRefreshTokenAndEmail(refresh_token, email);
        KhachHang khachHang = this.khachHangService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (nhanVien == null && khachHang == null) {
            throw new IdInvalidException("Refresh Token không hợp lệ ");
        }

        // issue new token/set refresh token as cookies
        ResLoginDTO res = new ResLoginDTO();
        if (nhanVien != null) {
            // Trigger lazy loading để tránh proxy issues
            Role role = nhanVien.getRole();
            String cuaHang = nhanVien.getCuaHang().getTenCuaHang();
            if (role != null) {
                role.getName(); // Trigger lazy loading
            }
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    nhanVien.getId(),
                    nhanVien.getEmail(),
                    nhanVien.getTenNhanVien(),
                    nhanVien.getSoDienThoai(),
                    nhanVien.getAvatar(),
                    role,
                    null,
                    cuaHang);
            res.setUser(userLogin);
        } else {
            // Trigger lazy loading để tránh proxy issues
            Role role = khachHang.getRole();
            if (role != null) {
                role.getName(); // Trigger lazy loading
            }
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    khachHang.getId(),
                    khachHang.getEmail(),
                    khachHang.getTenKhachHang(),
                    khachHang.getSdt(),
                    khachHang.getAvatar(),
                    role,
                    khachHang.getDiemTichLuy(),
                    null);
            res.setUser(userLogin);
        }

        // create access token
        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

        // update user
        this.nhanVienService.updateUserToken(new_refresh_token, email);
        this.khachHangService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access Token không hợp lệ");
        }
        // update refresh token = null
        this.nhanVienService.updateUserToken(null, email);
        this.khachHangService.updateUserToken(null, email);
        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }

    @PutMapping("/auth/change-password")
    @ApiMessage("Đổi mật khẩu thành công")
    public ResponseEntity<Void> changePassword(@RequestBody ReqChangePasswordDTO req) throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.isBlank()) {
            throw new IdInvalidException("Không xác định được người dùng hiện tại");
        }

        if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank()) {
            throw new IdInvalidException("Mật khẩu hiện tại không được để trống");
        }
        if (req.getNewPassword() == null || req.getNewPassword().isBlank()) {
            throw new IdInvalidException("Mật khẩu mới không được để trống");
        }
        if (req.getNewPassword().trim().length() < 6) {
            throw new IdInvalidException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        if (req.getConfirmPassword() == null || !req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new IdInvalidException("Xác nhận mật khẩu không khớp");
        }

        NhanVien nhanVien = nhanVienService.findByEmail(email);
        if (nhanVien != null) {
            if (!passwordEncoder.matches(req.getCurrentPassword(), nhanVien.getMatKhau())) {
                throw new IdInvalidException("Mật khẩu hiện tại không đúng");
            }
            nhanVienService.updatePasswordByEmail(email, passwordEncoder.encode(req.getNewPassword()));
            return ResponseEntity.ok().build();
        }

        KhachHang khachHang = khachHangService.findByEmail(email);
        if (khachHang != null) {
            if (!passwordEncoder.matches(req.getCurrentPassword(), khachHang.getPassword())) {
                throw new IdInvalidException("Mật khẩu hiện tại không đúng");
            }
            khachHangService.updatePasswordByEmail(email, passwordEncoder.encode(req.getNewPassword()));
            return ResponseEntity.ok().build();
        }

        throw new IdInvalidException("Không tìm thấy người dùng để đổi mật khẩu");
    }

    @PutMapping(value = "/auth/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Cập nhật thông tin người dùng thành công")
    public ResponseEntity<ResLoginDTO.UserGetAccount> updateProfile(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sdt", required = false) String sdt,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar)
            throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.isBlank()) {
            throw new IdInvalidException("Không xác định được người dùng hiện tại");
        }

        String phone = sdt == null ? null : sdt.trim();
        if (phone != null && !phone.isBlank() && !VIETNAM_PHONE_PATTERN.matcher(phone).matches()) {
            throw new IdInvalidException("Số điện thoại không hợp lệ. Dùng định dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.");
        }

        String avatarUrl = null;
        if (avatar != null && !avatar.isEmpty()) {
            avatarUrl = storageService.uploadSingleFile(avatar, "avatar");
        }

        NhanVien nhanVien = nhanVienService.findByEmail(email);
        if (nhanVien != null) {
            nhanVienService.updateProfileByEmail(email, name, phone, avatarUrl);
            return ResponseEntity.ok(new ResLoginDTO.UserGetAccount(buildUserLogin(email)));
        }

        KhachHang khachHang = khachHangService.findByEmail(email);
        if (khachHang != null) {
            khachHangService.updateProfileByEmail(email, name, phone, avatarUrl);
            return ResponseEntity.ok(new ResLoginDTO.UserGetAccount(buildUserLogin(email)));
        }

        throw new IdInvalidException("Không tìm thấy người dùng để cập nhật thông tin");
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody KhachHang postManUser)
            throws IdInvalidException {
        String phone = postManUser.getSdt() == null ? "" : postManUser.getSdt().trim();
        if (phone.isEmpty()) {
            throw new IdInvalidException("Số điện thoại không được để trống.");
        }
        if (!VIETNAM_PHONE_PATTERN.matcher(phone).matches()) {
            throw new IdInvalidException("Số điện thoại không hợp lệ. Dùng định dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.");
        }
        postManUser.setSdt(phone);

        boolean isEmailExist = this.khachHangService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + postManUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác.");
        }
        boolean isSdtExist = this.khachHangService.isSdtExist(postManUser.getSdt());
        if (isSdtExist) {
            throw new IdInvalidException(
                    "Số điện thoại " + postManUser.getSdt() + " đã tồn tại, vui lòng sử dụng số khác.");
        }
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        KhachHang savedUser = this.khachHangService.handleCreateUser(postManUser);

        // Tạo verification token và gửi email xác thực
        try {
            String token = this.verificationTokenService.createVerificationToken(savedUser);
            this.emailService.sendVerificationEmail(savedUser, token);
            logger.info("Email xác thực đã được gửi tới: {}", savedUser.getEmail());
        } catch (Exception ex) {
            logger.error("Lỗi khi gửi email xác thực cho người dùng {}: ", savedUser.getEmail(), ex);
            throw new IdInvalidException("Không thể gửi email xác thực. Lỗi: " + ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.khachHangService.convertToResCreateUserDTO(savedUser));
    }

    @GetMapping("/auth/confirm")
    @ApiMessage("Confirm email registration")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        boolean ok = this.verificationTokenService.validateVerificationToken(token);
        if (ok) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(buildConfirmSuccessPage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.TEXT_HTML)
                .body(buildConfirmErrorPage());
    }

    private String buildConfirmSuccessPage() {
        return """
                <!doctype html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Xác nhận thành công</title>
                    <style>
                        :root {
                            --bg-1: #f0fdf4;
                            --bg-2: #ecfeff;
                            --text: #052e16;
                            --subtext: #14532d;
                            --card: #ffffff;
                            --primary: #16a34a;
                            --primary-hover: #15803d;
                        }
                        * { box-sizing: border-box; }
                        body {
                            margin: 0;
                            min-height: 100vh;
                            display: grid;
                            place-items: center;
                            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                            color: var(--text);
                            background: radial-gradient(circle at top right, var(--bg-2), var(--bg-1));
                            padding: 24px;
                        }
                        .card {
                            width: min(640px, 100%);
                            background: var(--card);
                            border-radius: 20px;
                            padding: 32px;
                            box-shadow: 0 18px 45px rgba(5, 46, 22, 0.14);
                            text-align: center;
                        }
                        .icon {
                            width: 72px;
                            height: 72px;
                            border-radius: 50%;
                            margin: 0 auto 16px;
                            display: grid;
                            place-items: center;
                            background: rgba(22, 163, 74, 0.12);
                            font-size: 38px;
                        }
                        h1 {
                            margin: 0;
                            font-size: clamp(24px, 4vw, 32px);
                        }
                        p {
                            margin: 12px 0 0;
                            color: var(--subtext);
                            font-size: 16px;
                            line-height: 1.6;
                        }
                        .actions {
                            margin-top: 28px;
                        }
                        .btn {
                            display: inline-block;
                            background: var(--primary);
                            color: #ffffff;
                            text-decoration: none;
                            font-weight: 700;
                            padding: 12px 22px;
                            border-radius: 12px;
                            transition: background 0.2s ease, transform 0.2s ease;
                        }
                        .btn:hover {
                            background: var(--primary-hover);
                            transform: translateY(-1px);
                        }
                    </style>
                </head>
                <body>
                    <main class="card">
                        <div class="icon">✓</div>
                        <h1>Xác nhận đăng ký thành công</h1>
                        <p>Tài khoản của bạn đã được kích hoạt. Bấm nút bên dưới để đăng nhập vào hệ thống.</p>
                        <div class="actions">
                            <a class="btn" href="__FRONTEND_LOGIN_URL__">Đi tới trang đăng nhập</a>
                        </div>
                    </main>
                </body>
                </html>
                """.replace("__FRONTEND_LOGIN_URL__", frontendLoginUrl);
    }

    private String buildConfirmErrorPage() {
        return """
                <!doctype html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Xác nhận thất bại</title>
                    <style>
                        :root {
                            --bg-1: #fff1f2;
                            --bg-2: #fffbeb;
                            --text: #7f1d1d;
                            --subtext: #9f1239;
                            --card: #ffffff;
                        }
                        * { box-sizing: border-box; }
                        body {
                            margin: 0;
                            min-height: 100vh;
                            display: grid;
                            place-items: center;
                            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                            color: var(--text);
                            background: radial-gradient(circle at top right, var(--bg-2), var(--bg-1));
                            padding: 24px;
                        }
                        .card {
                            width: min(640px, 100%);
                            background: var(--card);
                            border-radius: 20px;
                            padding: 32px;
                            box-shadow: 0 18px 45px rgba(127, 29, 29, 0.12);
                            text-align: center;
                        }
                        .icon {
                            width: 72px;
                            height: 72px;
                            border-radius: 50%;
                            margin: 0 auto 16px;
                            display: grid;
                            place-items: center;
                            background: rgba(244, 63, 94, 0.16);
                            font-size: 38px;
                        }
                        h1 {
                            margin: 0;
                            font-size: clamp(24px, 4vw, 32px);
                        }
                        p {
                            margin: 12px 0 0;
                            color: var(--subtext);
                            font-size: 16px;
                            line-height: 1.6;
                        }
                    </style>
                </head>
                <body>
                    <main class="card">
                        <div class="icon">!</div>
                        <h1>Không thể xác nhận đăng ký</h1>
                        <p>Liên kết xác nhận không hợp lệ hoặc đã hết hạn. Vui lòng đăng ký lại để nhận liên kết mới.</p>
                    </main>
                </body>
                </html>
                """;
    }

    private ResLoginDTO.UserLogin buildUserLogin(String email) {
        NhanVien nhanVienDB = this.nhanVienService.findByEmail(email);
        if (nhanVienDB != null) {
            Role role = nhanVienDB.getRole();
            if (role != null) {
                role.getName();
            }
            String cuaHang = nhanVienDB.getCuaHang().getTenCuaHang();

            return new ResLoginDTO.UserLogin(
                    nhanVienDB.getId(),
                    nhanVienDB.getEmail(),
                    nhanVienDB.getTenNhanVien(),
                    nhanVienDB.getSoDienThoai(),
                    nhanVienDB.getAvatar(),
                    role,
                    null,
                    cuaHang);
        }

        KhachHang currentUserDB = this.khachHangService.findByEmail(email);
        if (currentUserDB != null) {
            Role role = currentUserDB.getRole();
            if (role != null) {
                role.getName();
            }
            return new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getTenKhachHang(),
                    currentUserDB.getSdt(),
                    currentUserDB.getAvatar(),
                    role,
                    currentUserDB.getDiemTichLuy(),
                    null);
        }

        return null;
    }
}
