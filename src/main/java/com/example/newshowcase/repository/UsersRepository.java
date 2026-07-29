package com.example.newshowcase.repository;

import com.example.newshowcase.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByLoginOrEmail(String login, String email);

    Optional<User> findByConfirmationCode(String confirmationCode);
}
