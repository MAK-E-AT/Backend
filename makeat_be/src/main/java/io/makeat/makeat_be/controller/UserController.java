package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.dto.AdditionalInfoDto;
import io.makeat.makeat_be.dto.FirstInfoDto;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final KakaoLoginService ks;
    private final NaverLoginService ns;
    private final UserService userService;


    @GetMapping("/kakao")
    public ResponseEntity getKakaoApi(HttpServletRequest request, @RequestParam String code) throws IOException{

        //인증코드로 토큰, 유저정보 GET
        String[] tokens = ks.getTokens(code);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        Map<String, Object> userInfo = ks.getUserInfo(accessToken);

        log.info("user info: " + userInfo);
        log.info("accessToken: " + accessToken);

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

    @PostMapping("/additional-info")
    public ResponseEntity saveAdditionalInfo (HttpServletRequest request, @RequestBody AdditionalInfoDto additionalInfoDto, @RequestParam String loginKind, @RequestParam String loginId) throws IOException{

        // user 확인 및 신규 유저 저장
        User user = userService.login(loginKind, loginId);
        if (user==null) {
            return new ResponseEntity(null, null, HttpStatus.BAD_REQUEST);
        }

        // 이전에 세션에 등록된 정보 불러오기
        HttpSession session = request.getSession();
        FirstInfoDto firstInfoDto = (FirstInfoDto) session.getAttribute("firstInfoDto");
        System.out.println(firstInfoDto);

        // 이전 세션 정보 + 추가정보 UserInfo 저장
        userService.saveUserInfo(additionalInfoDto, firstInfoDto, loginKind, loginId);

        return new ResponseEntity<>("", HttpStatus.OK);
    }


    @GetMapping("/naver")
    public ResponseEntity getNaverApi(HttpServletRequest request, @RequestParam String code) throws IOException, ParseException {

        //인증코드로 토큰, 유저정보 GET
        String[] tokens = ns.getTokens(code);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        Map<String, Object> userInfo = ks.getUserInfo(accessToken);

        // 네이버 userinfo에서 loginId, 이름, 성별 뽑기
        String loginId = userInfo.get("id").toString();
        String name = userInfo.get("nickname").toString();
        String gender = userInfo.get("gender").toString();

        log.info("id: " + loginId, ", name: " + name, ", gender: " + gender);

        // 이름, 성별, 액세스 토큰, 리프레쉬 토큰 세션에 등록
        FirstInfoDto firstInfoDto = new FirstInfoDto(name, gender, accessToken, refreshToken);

        HttpSession session = request.getSession();
        session.setAttribute("firstInfoDto", firstInfoDto);

        return new ResponseEntity(loginId, HttpStatus.OK);
    }

}