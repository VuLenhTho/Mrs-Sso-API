package com.vulenhtho.mrssso.repository;

import com.vulenhtho.mrssso.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
