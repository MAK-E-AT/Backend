package io.makeat.makeat_be.service;

import io.makeat.makeat_be.dto.NutrientDto;
import io.makeat.makeat_be.entity.Nutrient;
import io.makeat.makeat_be.repository.NutrientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NutrientService {
    private final NutrientRepository nutrientRepository;

    public void saveNutrient() {
        Nutrient nutrient = new Nutrient(10, 10, 10, 10, 10);
        nutrientRepository.save(nutrient);
    }

    public NutrientDto getTodayNutrients() {
        LocalDateTime todayStartDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime todayEndDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<Nutrient> todayNutrients = nutrientRepository.findByCreatedAtBetween(todayStartDateTime, todayEndDateTime);

        float totalCarbo = 0.0f;
        float totalProtein = 0.0f;
        float totalFat = 0.0f;
        float totalKcal = 0.0f;
        float totalNa = 0.0f;

        for (Nutrient nutrient : todayNutrients) {
            totalCarbo += nutrient.getCarbo();
            totalProtein += nutrient.getProtein();
            totalFat += nutrient.getFat();
            totalNa += nutrient.getNa();
            totalKcal += nutrient.getKcal();
        }

        NutrientDto nutrientDto = new NutrientDto(totalCarbo, totalProtein, totalFat, totalNa, totalKcal);

        return nutrientDto;
    }

    public float getThisWeekKcalTotal() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);
        List<Nutrient> nutrients = nutrientRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        float totalCalories = 0.0f;
        for (Nutrient nutrient : nutrients) {
            totalCalories += nutrient.getKcal();
        }
        return totalCalories;
    }
}
