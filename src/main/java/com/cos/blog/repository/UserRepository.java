package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);
}

    //JPA Naming 전략
    // SELECT & FROM user WHERE username =? AND password =? ;
//    User findByUsernameAndPassword(String username,String password);
