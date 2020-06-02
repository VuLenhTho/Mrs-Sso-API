package com.vulenhtho.mrssso.repository;

import com.vulenhtho.mrssso.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size,Long> {
    Size findByName(String name);
}
