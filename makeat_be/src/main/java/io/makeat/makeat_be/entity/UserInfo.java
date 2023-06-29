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

    private float target_calories;

    private float bmi;

    private String access_token;

    private String refresh_token;

    public UserInfo(long infoId, User user, String name, int age, String gender, float height, float weight, float target_calories, float bmi, String access_token, String refresh_token) {
        this.infoId = infoId;
        this.user = user;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.target_calories = target_calories;
        this.bmi = bmi;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
}