package io.makeat.makeat_be.repository;

import io.makeat.makeat_be.entity.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    List<Nutrient> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
