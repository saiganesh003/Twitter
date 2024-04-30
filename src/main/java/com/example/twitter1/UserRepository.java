package com.example.twitter1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
//    public default Optional<User> findByUsername(String username) {
//        return findByEmail(username);
//    }

}
