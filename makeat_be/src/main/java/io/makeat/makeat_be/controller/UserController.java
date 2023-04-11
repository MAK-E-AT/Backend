package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.dto.LoginRequest;
import io.makeat.makeat_be.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 해당 사이트에 회원 검증을 통해서 회원 정보를 받아옴
     * -> 받아온 회원 정보로 우리 db에 저장된 회원인지 여부 조회
     * -> 우리 회원이면 UserInfo 반환
     * -> 우리 회원이 아니면 null 반환
     *
     * @param loginRequest
     * @return UserInfo
     */
    @GetMapping
    public UserInfo responseUser(
            @RequestBody LoginRequest loginRequest,
            @RequestHeader("Authorization") String authorization
    ) {
        // service로 들어가는 로직
        return null;
    }

    /**
     * authorization에 넘어온 토큰 값으로 유저 탐색 후,
     * 해당 유저가 있으면 삭제. 없으면 null 반환
     *
     * @param authorization
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
     * @param userInfo
     */
    @PostMapping("/info")
    public void saveUserInfo(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserInfo userInfo
    ) {

    }

    @PutMapping("/info")
    public void modifyUserInfo(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserInfo userInfo
    ) {

    }
}
