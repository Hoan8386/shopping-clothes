package com.vn.shopping.controller;

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
import com.vn.shopping.domain.request.ReqLoginDTO;
import com.vn.shopping.service.KhachHangService;
import com.vn.shopping.service.NhanVienService;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final KhachHangService khachHangService;
    private final NhanVienService nhanVienService;
    private final PasswordEncoder passwordEncoder;

    @Value("${shopping.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            KhachHangService khachHangService,
            NhanVienService nhanVienService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.khachHangService = khachHangService;
        this.nhanVienService = nhanVienService;
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
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    nhanVien.getId(),
                    nhanVien.getEmail(),
                    nhanVien.getTenNhanVien(),
                    role);
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
                        role);
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
            userLogin.setRole(role);
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
                userLogin.setRole(role);
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
            if (role != null) {
                role.getName(); // Trigger lazy loading
            }
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    nhanVien.getId(),
                    nhanVien.getEmail(),
                    nhanVien.getTenNhanVien(),
                    role);
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
                    role);
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

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody KhachHang postManUser)
            throws IdInvalidException {
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
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.khachHangService.convertToResCreateUserDTO(savedUser));
    }
}
