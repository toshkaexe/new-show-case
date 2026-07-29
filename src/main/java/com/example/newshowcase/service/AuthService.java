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
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsersQueryRepository usersQueryRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsersQueryRepository usersQueryRepository, UsersRepository usersRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usersQueryRepository = usersQueryRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
        User newUser = new User(login, hashedPassword, email);
        newUser.setCreatedAt(Instant.now());
        usersRepository.save(newUser);
        log.info("User '{}' registered successfully", login);
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
