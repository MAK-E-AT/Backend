package io.makeat.makeat_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
@Service
public class KakaoLoginService {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_url}")
    private String redirectUrl;

    /**
     * 인가 코드로 토큰 반환
     * @param code
     * @return
     * @throws IOException
     */
    public String[] getTokens(String code) throws IOException {

        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        String[] tokens = new String[2];

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + clientId);
            sb.append("&redirect_uri=" + redirectUrl);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            log.info("responseCode = " + responseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("result = " + result);

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            tokens[0] = elem.get("access_token").toString();
            tokens[1] = elem.get("refresh_token").toString();
            log.info("access_token = " + tokens[0]);
            log.info("access_token = " + tokens[1]);
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    /**
     * 토큰으로 유저정보 파싱해서 반환
     * @param token
     * @return
     */
    public Map<String, Object> getUserInfo(String token) {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> result = new HashMap<>();
        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = connect(url.toString());
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            log.info("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            log.info("res = " + res);

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);

            // 로그 테스트 코드
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");

            log.info("kakao_account: " + kakao_account + "     properties: " + properties);
            // 로그 테스트 코드 끝

            String id = obj.get("id").toString();
            String nickname = ((JSONObject) obj.get("properties")).get("nickname").toString();
            String gender = ((JSONObject) obj.get("kakao_account")).get("gender").toString();

            result.put("id", id);
            result.put("nickname", nickname);
            result.put("gender", gender);

            log.info("id: " + id, ", nickname: " + nickname, ", gender: " + gender);

            br.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * API 연결
     * @param apiUrl
     * @return
     */
    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

}