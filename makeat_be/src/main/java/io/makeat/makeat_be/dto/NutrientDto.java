package io.makeat.makeat_be.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NutrientDto {
    private float carbo;
    private float protein;
    private float fat;
    private float na;
    private float kcal;

    public NutrientDto(float carbo, float protein, float fat, float na, float kcal) {
        this.carbo = carbo;
        this.protein = protein;
        this.fat = fat;
        this.na = na;
        this.kcal = kcal;
    }
}
