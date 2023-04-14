package io.makeat.makeat_be.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.makeat.makeat_be.utils.JwtUtil;

@Slf4j
@Service
public class LoginService {

//    @Value("{jwt.secret}")
    private String secretKey;

    public String login(String userName, String password, Long expireMs) {
        return JwtUtil.createJwt(userName, password, expireMs);
    }

}
