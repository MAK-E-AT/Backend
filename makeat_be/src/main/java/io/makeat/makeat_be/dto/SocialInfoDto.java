package io.makeat.makeat_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialInfoDto {
    private String login_kind;
    private String login_id;

    public SocialInfoDto(String login_kind, String login_id) {
        this.login_kind = login_kind;
        this.login_id = login_id;
    }
}
