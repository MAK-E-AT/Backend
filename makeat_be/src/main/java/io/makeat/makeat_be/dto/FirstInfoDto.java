package io.makeat.makeat_be.dto;

public class FirstInfoDto {

    private String name;
    private String gender;
    private String accessToken;
    private String refreshToken;

    public FirstInfoDto(String name, String gender, String accessToken, String refreshToken) {
        this.name = name;
        this.gender = gender;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
