package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class DummyControllerTest {


    private final UserRepository userRepository;


    @DeleteMapping("/user/{id}")
    public String delete(@PathVariable int id){
        try{
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            return "삭제 실패";
        }


        return "삭제되었습니다." + id;
    }

    @Transactional
    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable int id,@RequestBody User requestUser){

        log.info("id ={}, pw = {}, email = {}",id,requestUser.getPassword(),requestUser.getEmail());

        User user = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException();
        });
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        return user;
    }


    @GetMapping("/users")
    public List<User> list(){
        return userRepository.findAll();
    }

    @GetMapping("/user")
    public List<User> pageList(@PageableDefault(size=2,sort = "id",direction = Sort.Direction.DESC)
                               Pageable pageable){
        Page<User> pagingUser = userRepository.findAll(pageable);
        List<User> users = pagingUser.getContent();
        return users;
    }

    // 추후 aop를 이용한 에러 페이지를 보여줘야한다.
    @GetMapping("/user/{id}")
    public User detail(@PathVariable int id){
//        람다식
//        User user = userRepository.findById(id).orElseThrow(()-> {
//            return new IllegalArgumentException("없어요 유저");
//        });

        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("없어요 유저");
            }
        });

        return user;
    }

    @PostMapping("/join")
    public String join(User user){
        log.info("user id = {}" , user.getUsername());
        log.info("user password = {}" , user.getPassword());
        log.info("user email = {}" , user.getEmail());
        user.setRole(RoleType.USER);

        userRepository.save(user);

        return "회원가입이 잘 되었네요";
    }
}