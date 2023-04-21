package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.dto.UserInfoDto;
import io.makeat.makeat_be.entity.User;
import io.makeat.makeat_be.entity.UserInfo;
import io.makeat.makeat_be.repository.UserInfoRepository;
import io.makeat.makeat_be.repository.UserRepository;
import io.makeat.makeat_be.service.KakaoLoginService;
import io.makeat.makeat_be.service.LoginService;
import io.makeat.makeat_be.service.NaverLoginService;
import io.makeat.makeat_be.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     *
     * 프로트에서 카카오 인가코드를 /user/kakao 링크로 get 요청
     * -> 백에서 받아서 카카오 로그인 서버로 토큰 요청 -> 카카오 로그인 서버에서 토큰 반환
     * -> 토큰으로 카카오 로그인 서버에서 유저 정보 요청 -> 카카오 로그인 서버에서 유저 정보 반환
     * -> 유저 정보를 받아서 우리 서버로 유저 정보 요청 -> 우리 서버에서 유저 정보 반환 -> 유저 정보를 받아서 프론트로 유저 정보 반환
     *
     */

    @Autowired
    KakaoLoginService ks;

    @Autowired
    NaverLoginService ns;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @GetMapping("/kakao")
    public ResponseEntity getKakaoCI(@RequestParam String code, Model model) throws IOException{
        
        //인증코드로 토큰, 유저정보 GET
        String token = ks.getToken(code);
        Map<String, Object> userInfo = ks.getUserInfo(token);

        System.out.println("jun");
        // jwt를 생성하는 로직
        String jwt = loginService.login("kakao", (String) userInfo.get("loginId"));

        System.out.println("hu");

        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능

        String id = (String) userInfo.get("id");
        Optional<User> user = userRepository.findUserByLoginKindAndLoginId("kakao", id);

        if (user.isPresent()) {
            UserInfo UserInfo1 = userInfoRepository.findUserInfoByUser(user);
            UserInfoDto userInfoDto = new UserInfoDto();

            userInfoDto.setAge(UserInfo1.getAge());
            userInfoDto.setGender(UserInfo1.getGender());
            userInfoDto.setHeight(UserInfo1.getHeight());
            userInfoDto.setWeight(UserInfo1.getWeight());
            userInfoDto.setBmi(UserInfo1.getBmi());

            // HttpHeaders에 jwt를 담아서 프론트로 전달
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwt);

            return new ResponseEntity(userInfoDto, headers, HttpStatus.OK);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(null, headers, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/naver")
    public ResponseEntity getNaverCI(@RequestParam String code, Model model) throws IOException, ParseException {

        //인증코드로 토큰, 유저정보 GET
        String token = ns.getToken(code);
        Map<String, Object> userInfo = ns.getUserInfo(token);

        // jwt를 생성하는 로직
        String id = (String) userInfo.get("id");
        String jwt = loginService.login("naver", id);

        // ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능
        Optional<User> user = userRepository.findUserByLoginKindAndLoginId("naver", id);
        if (user.isPresent()) {
            UserInfo UserInfo1 = userInfoRepository.findUserInfoByUser(user);
            UserInfoDto userInfoDto = new UserInfoDto();

            userInfoDto.setAge(UserInfo1.getAge());
            userInfoDto.setGender(UserInfo1.getGender());
            userInfoDto.setHeight(UserInfo1.getHeight());
            userInfoDto.setWeight(UserInfo1.getWeight());
            userInfoDto.setBmi(UserInfo1.getBmi());

            // HttpHeaders에 jwt를 담아서 프론트로 전달
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwt);

            return new ResponseEntity(userInfoDto, headers, HttpStatus.OK);
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(null, headers, HttpStatus.BAD_REQUEST);
    }


    /**
     * 요청이 올때마다 jwt를 인코딩해서 사용자 정보를 검증해야햐는구나
     *
     */

    @DeleteMapping
    public void deleteUser(
            @RequestHeader("Authorization") String authorization
    ) {

    }

    /**
     * 1. Http 헤더 중 Authorization 을 받아 사용자 검증
     * 2. RequestParameter로 받은 userInfo를 해당 사용자에 저장
     *
     * @param userInfoDto
     */
    @PostMapping("/info")
    public ResponseEntity saveUserInfo(
            Authentication authentication,
            @RequestBody UserInfoDto userInfoDto
    ) {
        HttpHeaders headers = new HttpHeaders();

        // Authentication에서 loginId와 loginKind 가져오기
        String loginKind = (String) authentication.getCredentials();
        String loginId = authentication.getName();

        // User 정보 가져오기
        Optional<User> user = userRepository.findUserByLoginKindAndLoginId(loginKind, loginId);
        if (user == null) {
            // 검증되지 않은 사용자라면 404 에러 반환
            return new ResponseEntity(null, headers, HttpStatus.BAD_REQUEST);
        }

        // 사용자 정보 저장
        userService.saveUserInfo(userInfoDto);

        return new ResponseEntity(null, headers, HttpStatus.OK);
    }

    @PutMapping("/info")
    public void modifyUserInfo(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserInfoDto userInfoDto
    ) {

    }
}
