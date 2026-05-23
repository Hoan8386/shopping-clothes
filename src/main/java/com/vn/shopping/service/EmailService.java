package com.vn.shopping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.vn.shopping.domain.KhachHang;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${shopping.base-url}")
    private String baseUrl;

    @Value("${shopping.frontend-reset-password-url}")
    private String frontendResetPasswordUrl;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Value("${spring.mail.from:}")
    private String configuredFromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(KhachHang user, String token) throws MessagingException {
        try {
            String confirmUrl = baseUrl + "/api/v1/auth/confirm?token=" + token;
            String fullName = user.getTenKhachHang() == null ? "" : user.getTenKhachHang();

            // Tạo HTML email
            String htmlContent = "<html><body>" +
                    "<h2>Xác nhận đăng ký tài khoản</h2>" +
                    "<p>Xin chào " + fullName + ",</p>" +
                    "<p>Vui lòng bấm vào link sau để xác nhận đăng ký tài khoản:</p>" +
                    "<p><a href=\"" + confirmUrl + "\" style=\"display: inline-block; padding: 10px 20px; " +
                    "background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;\">" +
                    "Xác nhận Email</a></p>" +
                    "<p>Hoặc copy link này vào trình duyệt: " + confirmUrl + "</p>" +
                    "<p>Link này sẽ hết hạn trong 24 giờ.</p>" +
                    "<p>Nếu bạn không đăng ký, vui lòng bỏ qua email này.</p>" +
                    "</body></html>";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String mailFrom = (configuredFromAddress != null && !configuredFromAddress.isBlank())
                    ? configuredFromAddress
                    : fromAddress;
            if (mailFrom == null || mailFrom.isBlank()) {
                throw new IllegalStateException(
                        "Chua cau hinh spring.mail.username hoac spring.mail.from cho he thong gui email");
            }

            helper.setTo(user.getEmail());
            helper.setFrom(mailFrom);
            helper.setSubject("Xác nhận đăng ký tài khoản");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("Xác thực email được gửi thành công tới: {}", user.getEmail());
        } catch (MessagingException e) {
            logger.error("Lỗi khi gửi email xác thực tới {}: {}", user.getEmail(), e.getMessage(), e);
            throw new MessagingException("Không thể gửi email xác thực: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi gửi email xác thực: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordResetEmail(String email, String fullName, String resetUrl) throws MessagingException {
        try {
            String displayName = (fullName == null || fullName.isBlank()) ? email : fullName;

            String htmlContent = "<html><body>" +
                    "<h2>Yêu cầu đặt lại mật khẩu</h2>" +
                    "<p>Xin chào " + displayName + ",</p>" +
                    "<p>Bạn vừa gửi yêu cầu quên mật khẩu. Bấm vào link bên dưới để cập nhật mật khẩu mới:</p>" +
                    "<p><a href=\"" + resetUrl + "\" style=\"display: inline-block; padding: 10px 20px; " +
                    "background-color: #2563eb; color: white; text-decoration: none; border-radius: 5px;\">" +
                    "Đặt lại mật khẩu</a></p>" +
                    "<p>Hoặc copy link này vào trình duyệt: " + resetUrl + "</p>" +
                    "<p>Liên kết này sẽ hết hạn trong thời gian ngắn để đảm bảo an toàn.</p>" +
                    "<p>Nếu bạn không yêu cầu đổi mật khẩu, vui lòng bỏ qua email này.</p>" +
                    "</body></html>";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String mailFrom = (configuredFromAddress != null && !configuredFromAddress.isBlank())
                    ? configuredFromAddress
                    : fromAddress;
            if (mailFrom == null || mailFrom.isBlank()) {
                throw new IllegalStateException(
                        "Chua cau hinh spring.mail.username hoac spring.mail.from cho he thong gui email");
            }

            helper.setTo(email);
            helper.setFrom(mailFrom);
            helper.setSubject("Đặt lại mật khẩu");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("Email đặt lại mật khẩu được gửi thành công tới: {}", email);
        } catch (MessagingException e) {
            logger.error("Lỗi khi gửi email đặt lại mật khẩu tới {}: {}", email, e.getMessage(), e);
            throw new MessagingException("Không thể gửi email đặt lại mật khẩu: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi gửi email đặt lại mật khẩu: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage(), e);
        }
    }
}
