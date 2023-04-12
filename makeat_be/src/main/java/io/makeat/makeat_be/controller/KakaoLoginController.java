package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
public class KakaoLoginController {
    @Autowired
    KakaoLoginService ks;

    /**
     * 인가코드 받는 컨트롤러-프론트단 대리 테스트용
     * @return
     */
    @GetMapping("/user/do")
    public String loginPage()
    {
        return "login";
    }

    @ResponseBody
    @GetMapping("/user/kakao")
    public String getCI(@RequestParam String code, Model model) throws IOException {
        System.out.println("code = " + code);
        String[] tokens = ks.getToken(code);
        String access_token = tokens[0];
        String refresh_token = tokens[1];
        Map<String, Object> userInfo = ks.getUserInfo(access_token);
        //model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("refresh_token", refresh_token);
        model.addAttribute("userInfo", userInfo);

        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능
        return model.toString();
    }
}