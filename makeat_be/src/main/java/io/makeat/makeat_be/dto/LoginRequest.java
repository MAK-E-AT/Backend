package io.makeat.makeat_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String loginUrl;    // 검증할 사이트 주소

    private String authorization;   // 인증 토큰값

    private String clientId;    // 클라이언트 id

    private String clientSecret;    // 사용자의 비밀키

    private String redirectUrl; // redirection 주소
}
