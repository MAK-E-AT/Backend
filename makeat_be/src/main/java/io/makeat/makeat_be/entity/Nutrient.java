package io.makeat.makeat_be.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Nutrient {
    @Id
    @GeneratedValue
    @Column(name = "nutrient_id")
    private Long nutrientId;

    private float carbo;
    private float protein;
    private float fat;
    private float na;
    private float kcal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Nutrient() {

    }

    public Nutrient(float carbo, float protein, float fat, float na, float kcal) {
        this.carbo = carbo;
        this.protein = protein;
        this.fat = fat;
        this.na = na;
        this.kcal = kcal;
    }
}
