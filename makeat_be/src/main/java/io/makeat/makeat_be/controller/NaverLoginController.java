package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.service.NaverLoginService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NaverLoginController {
    @Autowired
    NaverLoginService ns;

    @ResponseBody
    @GetMapping("/user/naver")
    public String getCI(@RequestParam String code, Model model) throws IOException, ParseException {
        //인증코드로 토큰 받아오는 코드
        //액세스, 리프레쉬 코드 model에 저장

        String[] tokens = ns.getToken(code);
        String access_token = tokens[0];
        String refresh_token = tokens[1];

        //여기까지가 액세스, 리프레쉬토큰값 받아오기

        String header = "Bearer " + access_token; // Bearer 다음에 공백 추가
        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        model.addAttribute("access_token", access_token);
        model.addAttribute("refresh_token", refresh_token);

        //사용자정보 json ns.getApi(apiURL,requestHeaders)
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(ns.getApi(apiURL,requestHeaders));
        JSONObject response = (JSONObject) jsonObj.get("response");

        String name = response.get("name").toString();
        String gender = response.get("gender").toString();

        model.addAttribute("name", name);
        model.addAttribute("gender", gender);

        return model.toString();
    }
}
