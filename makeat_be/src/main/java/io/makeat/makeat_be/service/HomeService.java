package io.makeat.makeat_be.service;

import io.makeat.makeat_be.entity.Nutrient;
import io.makeat.makeat_be.entity.User;
import io.makeat.makeat_be.entity.UserInfo;
import io.makeat.makeat_be.repository.NutrientRepository;
import io.makeat.makeat_be.repository.UserInfoRepository;
import io.makeat.makeat_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserInfo getUserInfo(String loginKind, String loginId){
        User user = userRepository.findUserByLoginKindAndLoginId(loginKind, loginId);
        log.info("user: " + user.getLoginId());

        UserInfo userInfo = userInfoRepository.findUserInfoByUser(Optional.of(user));
        log.info("userinfo: " + userInfo.getName());

        return userInfo;
    }
}
