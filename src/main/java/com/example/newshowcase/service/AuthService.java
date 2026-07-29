package com.example.newshowcase.service;

import com.example.newshowcase.exception.BadRequestException;
import com.example.newshowcase.model.User;
import com.example.newshowcase.repository.UsersQueryRepository;
import com.example.newshowcase.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final long CONFIRMATION_CODE_EXPIRATION_HOURS = 24;

    private final UsersQueryRepository usersQueryRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(UsersQueryRepository usersQueryRepository, UsersRepository usersRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.usersQueryRepository = usersQueryRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public Map<String, String> login(String loginOrEmail, String password) {
        Optional<User> optUser = usersQueryRepository.findByLoginOrEmail(loginOrEmail);

        if (optUser.isEmpty()) {
            log.warn("Login failed: user not found for '{}'", loginOrEmail);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        User user = optUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed: invalid password for user '{}'", loginOrEmail);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getLogin());
        log.info("User '{}' logged in successfully", user.getLogin());
        return Map.of("accessToken", accessToken);
    }

    public void registration(String login, String email, String password) {
        Optional<User> existingByLogin = usersQueryRepository.findByLoginOrEmail(login);
        if (existingByLogin.isPresent()) {
            log.warn("Registration failed: login '{}' already exists", login);
            throw new BadRequestException("User with this login already exists", "login");
        }

        Optional<User> existingByEmail = usersQueryRepository.findByLoginOrEmail(email);
        if (existingByEmail.isPresent()) {
            log.warn("Registration failed: email '{}' already exists", email);
            throw new BadRequestException("User with this email already exists", "email");
        }

        String hashedPassword = passwordEncoder.encode(password);
        String confirmationCode = UUID.randomUUID().toString();

        User newUser = new User(login, hashedPassword, email);
        newUser.setCreatedAt(Instant.now());
        newUser.setConfirmationCode(confirmationCode);
        newUser.setConfirmationCodeExpiration(Instant.now().plus(CONFIRMATION_CODE_EXPIRATION_HOURS, ChronoUnit.HOURS));
        usersRepository.save(newUser);

        emailService.sendConfirmationEmail(email, confirmationCode);
        log.info("User '{}' registered successfully, confirmation email sent", login);
    }

    public void confirmRegistration(String code) {
        Optional<User> optUser = usersRepository.findByConfirmationCode(code);

        if (optUser.isEmpty()) {
            log.warn("Confirmation failed: invalid code");
            throw new BadRequestException("Confirmation code is incorrect or already been applied", "code");
        }

        User user = optUser.get();

        if (user.isConfirmed()) {
            log.warn("Confirmation failed: user '{}' already confirmed", user.getLogin());
            throw new BadRequestException("Confirmation code is already been applied", "code");
        }

        if (user.getConfirmationCodeExpiration() != null && Instant.now().isAfter(user.getConfirmationCodeExpiration())) {
            log.warn("Confirmation failed: code expired for user '{}'", user.getLogin());
            throw new BadRequestException("Confirmation code is expired", "code");
        }

        user.setConfirmed(true);
        user.setConfirmationCode(null);
        user.setConfirmationCodeExpiration(null);
        usersRepository.save(user);
        log.info("User '{}' confirmed registration", user.getLogin());
    }

    public Map<String, String> getMe(String userId) {
        var user = usersQueryRepository.getById(userId);

        if (user.isEmpty()) {
            log.warn("getMe failed: user with id '{}' not found", userId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        var u = user.get();
        return Map.of(
                "email", u.getEmail(),
                "login", u.getLogin(),
                "userId", u.getId()
        );
    }
}
