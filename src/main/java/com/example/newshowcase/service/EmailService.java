package com.example.newshowcase.service;

import com.example.newshowcase.model.User;
import com.example.newshowcase.repository.UsersQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final UsersQueryRepository usersQueryRepository;

    public EmailService(UsersQueryRepository usersQueryRepository) {
        this.usersQueryRepository = usersQueryRepository;
    }

    public void resendConfirmationEmail(String email) {
        Optional<User> user = usersQueryRepository.findByLoginOrEmail(email);

        if (user.isEmpty()) {
            log.info("Email resend requested for non-existing user: {}", email);
            return;
        }

        String confirmationCode = UUID.randomUUID().toString();
        String confirmationLink = "https://some-front.com/confirm-registration?code=" + confirmationCode;

        log.info("Sending confirmation email to '{}', code={}", email, confirmationCode);
        // TODO: integrate with real email provider
    }
}
