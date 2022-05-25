package com.example.projectserver.uniserver.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDAO extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
}
