package com.evrensesi.evrensesi.repository;

import com.evrensesi.evrensesi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUsernameOrEmail(String username, String email);
}
