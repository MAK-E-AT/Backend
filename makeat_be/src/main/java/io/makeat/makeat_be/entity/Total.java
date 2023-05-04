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
public class Total {
    @Id @GeneratedValue
    @Column(name = "total_id")
    private long totalId;
}
