package com.cos.blog.controller.api;

import com.cos.blog.Config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;


    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user){
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1); //자바오브젝트가 JSON으로 변환해서 리턴(JackSON)
    }

    @PutMapping("/user")
    public ResponseDto<Integer> update(@RequestBody User user,
                                       @AuthenticationPrincipal PrincipalDetail principal,
                                       HttpSession session){

        userService.회원수정(user);

        //세션등록

        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("user.getUsername ={}", user.getUsername());
        log.info("user.getPassword ={}", user.getPassword());

        return new ResponseDto<Integer>(HttpStatus.OK.value(),1); //자바오브젝트가 JSON으로 변환해서 리턴(JackSON)
    }




        //여기서 트랜잭션이 종료되기 떄문에 DB에 값은 변경이 됐음.
        // 하지만 세션값은 변경되지 않은 상태이기 떄문 우리가 직접 세션값을 변경해줄것임
        // 강제적 세션값 변경
        // 실패 버전 Authentication을 직접 넣어주는건 안됨
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(principal,null,principal.getAuthorities());
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        session.setAttribute("SPRING_SECURITY_CONTEXT",securityContext);

    //전통적인 로그인 방법
//    @PostMapping("/api/user/login")
//    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session){
//        User principal = userService.로그인(user); // principal =  접근주체
//
//        if(principal != null){
//            session.setAttribute("principal",principal);
//        }
//        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
//    }
}