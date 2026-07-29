package com.example.newshowcase.service;

import com.example.newshowcase.model.User;
import com.example.newshowcase.repository.UsersQueryRepository;
import com.example.newshowcase.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final UsersQueryRepository usersQueryRepository;
    private final UsersRepository usersRepository;
    private final JavaMailSender mailSender;

    public EmailService(UsersQueryRepository usersQueryRepository, UsersRepository usersRepository,
                        JavaMailSender mailSender) {
        this.usersQueryRepository = usersQueryRepository;
        this.usersRepository = usersRepository;
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String email, String confirmationCode) {
        String confirmationLink = "https://some-front.com/confirm-registration?code=" + confirmationCode;
        sendEmail(email, "Confirm your registration", buildConfirmationHtml(confirmationLink, confirmationCode));
        log.info("Confirmation email sent to '{}'", email);
    }

    public void sendPasswordRecoveryEmail(String email) {
        Optional<User> optUser = usersQueryRepository.findByLoginOrEmail(email);

        if (optUser.isEmpty()) {
            log.info("Password recovery requested for non-existing email: {}", email);
            return;
        }

        User user = optUser.get();
        String recoveryCode = UUID.randomUUID().toString();
        user.setRecoveryCode(recoveryCode);
        user.setRecoveryCodeExpiration(Instant.now().plus(1, ChronoUnit.HOURS));
        usersRepository.save(user);

        String recoveryLink = "https://some-front.com/password-recovery?recoveryCode=" + recoveryCode;
        sendEmail(email, "Password Recovery", buildRecoveryHtml(recoveryLink, recoveryCode));
        log.info("Password recovery email sent to '{}'", email);
    }

    public void resendConfirmationEmail(String email) {
        Optional<User> optUser = usersQueryRepository.findByLoginOrEmail(email);

        if (optUser.isEmpty()) {
            log.info("Email resend requested for non-existing user: {}", email);
            return;
        }

        User user = optUser.get();

        if (user.isConfirmed()) {
            log.info("Email resend requested for already confirmed user: {}", email);
            return;
        }

        String confirmationCode = UUID.randomUUID().toString();
        user.setConfirmationCode(confirmationCode);
        user.setConfirmationCodeExpiration(Instant.now().plus(24, ChronoUnit.HOURS));
        usersRepository.save(user);

        sendConfirmationEmail(email, confirmationCode);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("zurix@mail.ru");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to '{}': {}", to, e.getMessage());
        }
    }

    private String buildConfirmationHtml(String link, String code) {
        return """
                <html>
                <body>
                    <h1>Registration Confirmation</h1>
                    <p>Please confirm your registration by clicking the link below:</p>
                    <a href="%s">Confirm Registration</a>
                    <p>Or use this code: <strong>%s</strong></p>
                </body>
                </html>
                """.formatted(link, code);
    }

    private String buildRecoveryHtml(String link, String code) {
        return """
                <html>
                <body>
                    <h1>Password Recovery</h1>
                    <p>To reset your password, click the link below:</p>
                    <a href="%s">Reset Password</a>
                    <p>Or use this recovery code: <strong>%s</strong></p>
                    <p>This code expires in 1 hour.</p>
                </body>
                </html>
                """.formatted(link, code);
    }
}
