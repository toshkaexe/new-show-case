package com.example.newshowcase.service;

import com.example.newshowcase.model.User;
import com.example.newshowcase.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String create(String login, String password, String email) {
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(login, hashedPassword, email);
        User saved = usersRepository.save(newUser);
        log.info("User created: id={}, login='{}'", saved.getId(), saved.getLogin());
        return saved.getId();
    }

    public boolean removeById(String id) {
        if (!usersRepository.existsById(id)) {
            return false;
        }
        usersRepository.deleteById(id);
        log.info("User deleted: id={}", id);
        return true;
    }

    public void deleteAll() {
        usersRepository.deleteAll();
        log.warn("All users deleted");
    }
}
