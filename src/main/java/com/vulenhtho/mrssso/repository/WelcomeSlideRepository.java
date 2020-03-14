package com.vulenhtho.mrssso.repository;

import com.vulenhtho.mrssso.entity.WelcomeSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WelcomeSlideRepository extends JpaRepository<WelcomeSlide, Long> {
    List<WelcomeSlide> getByIsDisabled(Boolean isDisabled);
}
