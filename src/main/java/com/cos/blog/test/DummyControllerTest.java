package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DummyControllerTest {


    private final UserRepository userRepository;

    @PostMapping("/dummy/join")
    public String join(User user){
        log.info("user id = {}" , user.getUsername());
        log.info("user password = {}" , user.getPassword());
        log.info("user email = {}" , user.getEmail());
        user.setRole(RoleType.USER);

        userRepository.save(user);

        return "회원가입이 잘 되었네요";
    }
}