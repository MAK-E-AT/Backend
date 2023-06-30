package io.makeat.makeat_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalInfoDto {

    private int age;
    private float height;
    private float weight;
    private float targetCalories;

    public AdditionalInfoDto(int age, float height, float weight, float targetCalories) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.targetCalories = targetCalories;
    }
}
