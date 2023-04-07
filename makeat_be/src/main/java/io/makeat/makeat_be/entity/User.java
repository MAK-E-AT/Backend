package io.makeat.makeat_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_kind")
    private String loginKind;

    @Column(name = "login_id")
    private Long loginId;
}
