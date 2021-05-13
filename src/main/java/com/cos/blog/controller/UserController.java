package com.cos.blog.controller;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

// 인증이 안된 사용자들이 출입할수있는 경로를 /auth/** 허용
// 그냥 주소가 / saveForm.jsp 허용
// static 이하에 있는 file 허

@Slf4j
@Controller
public class UserController {

    @Value("${cos.key}")
    private String cosKey;

    @Autowired
    private UserService userService;

    @Autowired
    private  AuthenticationManager authenticationManager;


    @GetMapping("/auth/joinForm")
    public String joinForm() {

        return "/user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {

        return "/user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {

        return "/user/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수

        // post방식으로 key=value 데이터를 요청(카카오쪽으로)

        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트생성
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","9027739767f1c5a2a73d23d64b07e7da");
        params.add("redirect_uri","http://localhost:8000/auth/kakao/callback");
        params.add("code",code);

        //HttpHeader ,HttpBody 하나의 오브젝트에 담
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params,headers);

        //Http 요청하기 - post 방식으로 - 그리고 response 응답받음
        ResponseEntity<String> responseEntity = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // Gson, Json Simple ,ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken authToken = null;
        try {
            authToken = objectMapper.readValue(responseEntity.getBody(),OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("카카오엑세스 토근 : ={}",authToken.getAccess_token());


        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization","Bearer "+authToken.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader ,HttpBody 하나의 오브젝트에 담
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest2 =
                new HttpEntity<>(headers2 );

        //Http 요청하기 - post 방식으로 - 그리고 response 응답받음
        ResponseEntity<String> responseEntity2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoTokenRequest2,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(responseEntity2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("카카오아이디 : ={}",kakaoProfile.getId());
        log.info("카카오이메 : ={}",kakaoProfile.getKakao_account().getEmail());

        log.info("블로그서버 아이디 : ={}",kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        log.info("블로그서버 이메일 : ={}",kakaoProfile.getKakao_account().getEmail());
        //UUID란 -> 중복되지 않는 어떤 특정값을 만들어내는 알고리즘
        UUID garbagePassword = UUID.randomUUID();
        log.info("블로그서버 패스워드 : ={}",cosKey);

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(cosKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        //회원가입철
        User originUser = userService.회원찾기(kakaoUser.getUsername());


        if(originUser.getUsername() == null){
            userService.회원가입(kakaoUser);
        }

        //로그인처리

        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(),cosKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }

}