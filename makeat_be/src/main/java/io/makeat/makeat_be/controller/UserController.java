package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.dto.AdditionalInfoDto;
import io.makeat.makeat_be.dto.FirstInfoDto;
import io.makeat.makeat_be.dto.SocialInfoDto;
import io.makeat.makeat_be.dto.UserInfoDto;
import io.makeat.makeat_be.entity.User;
import io.makeat.makeat_be.service.KakaoLoginService;
import io.makeat.makeat_be.service.NaverLoginService;
import io.makeat.makeat_be.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final KakaoLoginService ks;
    private final NaverLoginService ns;
    private final UserService userService;


    @GetMapping("/kakao")
    public ResponseEntity saveKakaoUser(HttpServletRequest request, @RequestParam String code) throws IOException{

        //인증코드로 토큰, 유저정보 GET
        String[] tokens = ks.getTokens(code);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        Map<String, Object> userInfo = ks.getUserInfo(accessToken);

        // user 확인 및 신규 유저 저장
        User user = userService.login("kakao", userInfo.get("id").toString());
        if (user==null) {
            return new ResponseEntity(null, null, HttpStatus.BAD_REQUEST);
        }

        // 카카오 userinfo에서 loginId, 이름, 성별 뽑기
        String loginId = userInfo.get("id").toString();
        String name = userInfo.get("nickname").toString();
        String gender = userInfo.get("gender").toString();

        // 이름, 성별, 액세스 토큰, 리프레쉬 토큰 세션에 등록
        FirstInfoDto firstInfoDto = new FirstInfoDto(name, gender, accessToken, refreshToken);

        HttpSession session = request.getSession();
        session.setAttribute("firstInfoDto", firstInfoDto);

        return new ResponseEntity<>(loginId, HttpStatus.OK);
    }

    @GetMapping("/additional-info")
    public ResponseEntity saveAdditionalInfo (HttpServletRequest request, AdditionalInfoDto additionalInfoDto, SocialInfoDto socialInfoDto) throws IOException{

        // 이전에 세션에 등록된 정보 불러오기
        HttpSession session = request.getSession();
        FirstInfoDto firstInfoDto = (FirstInfoDto) session.getAttribute("firstInfoDto");

        // 이전 세션 정보 + 추가정보 UserInfo 저장
        userService.saveUserInfo(socialInfoDto, additionalInfoDto, firstInfoDto);

        return new ResponseEntity<>("", HttpStatus.OK);
    }


    @GetMapping("/naver")
    public ResponseEntity saveNaverUser(@RequestParam String code) throws IOException, ParseException {

        //인증코드로 토큰, 유저정보 GET
        String token = ns.getToken(code);
        Map<String, Object> userInfo = ns.getUserInfo(token);

        // user 확인 및 신규 유저 저장
        User user = userService.login("naver", userInfo.get("id").toString());
        if (user==null) {
            return new ResponseEntity(null, null, HttpStatus.BAD_REQUEST);
        }

        UserInfoDto userInfoDto = userService.getUserInfo(user);

        return new ResponseEntity(userInfoDto, null, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(
            Authentication authentication
    ) {
        String userPk = (String) authentication.getCredentials();
        if (userPk == null) {
            // 검증되지 않은 사용자라면 404 에러 반환
            return new ResponseEntity(null, null, HttpStatus.BAD_REQUEST);
        }

        userService.deleteUser(userPk);
        return new ResponseEntity(null, null, HttpStatus.OK);
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
        String userPk = (String) authentication.getCredentials();

        if (userPk == null) {
            // 검증되지 않은 사용자라면 404 에러 반환
            return new ResponseEntity(null, null, HttpStatus.BAD_REQUEST);
        }

        // 사용자 정보 저장
//        userService.saveUserInfo(userInfoDto, userPk);

        return new ResponseEntity(null, null, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity modifyUserInfo(
            @RequestBody UserInfoDto userInfoDto,
            Authentication authentication
    ) {
        String userPk = (String) authentication.getCredentials();
        if (userPk == null) {
            // 검증되지 않은 사용자라면 404 에러 반환
            return new ResponseEntity(null, null, HttpStatus.BAD_REQUEST);
        }

        // 사용자 수정
        userService.modifyUserInfo(userInfoDto, userPk);

        return new ResponseEntity(null, null, HttpStatus.OK);
    }
}
