package com.example.newshowcase.features.users.service;

import com.example.newshowcase.features.users.domain.User;
import com.example.newshowcase.features.users.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

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
        return saved.getId();
    }

    public boolean removeById(String id) {
        if (!usersRepository.existsById(id)) {
            return false;
        }
        usersRepository.deleteById(id);
        return true;
    }

    public void deleteAll() {
        usersRepository.deleteAll();
    }
}
