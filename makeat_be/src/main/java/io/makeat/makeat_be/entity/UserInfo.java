package io.makeat.makeat_be.entity;

import io.makeat.makeat_be.dto.UserInfoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserInfo {
    @Id
    @GeneratedValue
    @Column(name = "info_id")
    private long infoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private int age;

    private String gender; // M, W

    private float height;

    private float weight;

    private float targetCalories;

    private float bmi;

    private String accessToken;

    private String refreshToken;

    public UserInfo(User user, String name, int age, String gender, float height, float weight, float targetCalories, float bmi, String accessToken, String refreshToken) {
        this.user = user;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.targetCalories = targetCalories;
        this.bmi = bmi;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}