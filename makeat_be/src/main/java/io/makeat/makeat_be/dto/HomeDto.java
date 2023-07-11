package io.makeat.makeat_be.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class HomeDto {
    private int age;
    private String gender;
    private float height;
    private float weight;
    private float bmi;
    private float targetCalories;

    private float thisWeekKcalTotal;

    private float todayCarbo;
    private float todayProtein;
    private float todayFat;
    private float todayNa;
    private float todayKcal;

    public HomeDto() {
    }

    public HomeDto(int age, String gender, float height, float weight, float bmi, float targetCalories, float thisWeekKcalTotal, float todayCarbo, float todayProtein, float todayFat, float todayNa, float todayKcal) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.targetCalories = targetCalories;
        this.thisWeekKcalTotal = thisWeekKcalTotal;
        this.todayCarbo = todayCarbo;
        this.todayProtein = todayProtein;
        this.todayFat = todayFat;
        this.todayNa = todayNa;
        this.todayKcal = todayKcal;
    }
}
