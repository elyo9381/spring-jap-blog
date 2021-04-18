package com.cos.blog.service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public int 회원가입(User user){
        try{
            userRepository.save(user);
            return 1;
        } catch (Exception e){
            e.printStackTrace();
            log.info("UserService : 회원가입 : {}" ,e.getMessage());
        }
        return -1;
    }
}