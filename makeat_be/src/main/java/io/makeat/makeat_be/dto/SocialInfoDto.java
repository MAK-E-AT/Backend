package io.makeat.makeat_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialInfoDto {
    private String loginKind;
    private String loginId;

    public SocialInfoDto(String loginKind, String loginId) {
        this.loginKind = loginKind;
        this.loginId = loginId;
    }
}
