package com.example.newshowcase.service;

import com.example.newshowcase.model.User;
import com.example.newshowcase.repository.UsersRepository;
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
