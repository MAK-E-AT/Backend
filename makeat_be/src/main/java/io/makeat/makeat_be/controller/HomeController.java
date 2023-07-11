package io.makeat.makeat_be.controller;

import io.makeat.makeat_be.dto.HomeDto;
import io.makeat.makeat_be.dto.NutrientDto;
import io.makeat.makeat_be.entity.UserInfo;
import io.makeat.makeat_be.service.HomeService;
import io.makeat.makeat_be.service.NutrientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/v1/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;
    private final NutrientService nutrientService;

    @PostMapping
    public HomeDto getHomeDto(@RequestParam String loginKind, @RequestParam String loginId) throws IOException{

        // 내 신체정보
        UserInfo userInfo = homeService.getUserInfo(loginKind, loginId);

        // 오늘 섭취 영양성분
        NutrientDto todayNutrients = nutrientService.getTodayNutrients();

        // 주간 칼로리 누적 섭취량
        float thisWeekKcalTotal = nutrientService.getThisWeekKcalTotal();

        HomeDto homeDto = new HomeDto(userInfo.getAge(), userInfo.getGender(), userInfo.getHeight(), userInfo.getWeight(), userInfo.getBmi(), userInfo.getTargetCalories(), thisWeekKcalTotal, todayNutrients.getCarbo(), todayNutrients.getProtein(), todayNutrients.getFat(), todayNutrients.getNa(), todayNutrients.getKcal());

        return homeDto;
    }

    // nutrient 테스트 데이터 넣고 오늘 섭취 영양성분 리스트 출력 컨트롤러
    @PostMapping("/abc")
    public HomeDto testHome(@RequestParam String loginKind, @RequestParam String loginId) throws IOException {

        nutrientService.saveNutrient();
        nutrientService.saveNutrient();
        nutrientService.saveNutrient();

        // 내 신체정보
        UserInfo userInfo = homeService.getUserInfo(loginKind, loginId);

        // 오늘
        NutrientDto todayNutrients = nutrientService.getTodayNutrients();
        log.info("오늘 영양정보: " + todayNutrients.toString());

        // 이번 주
        float thisWeekKcalTotal = nutrientService.getThisWeekKcalTotal();
        log.info("이번주 칼로리: " + thisWeekKcalTotal);

        HomeDto homeDto = new HomeDto(userInfo.getAge(), userInfo.getGender(), userInfo.getHeight(), userInfo.getWeight(), userInfo.getBmi(), userInfo.getTargetCalories(), thisWeekKcalTotal, todayNutrients.getCarbo(), todayNutrients.getProtein(), todayNutrients.getFat(), todayNutrients.getNa(), todayNutrients.getKcal());
        log.info("homeDto: " + homeDto);

        return homeDto;
    }
}