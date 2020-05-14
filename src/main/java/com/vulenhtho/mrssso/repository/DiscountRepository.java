package com.vulenhtho.mrssso.repository;

import com.vulenhtho.mrssso.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT d FROM Discount d WHERE d.startDate < ?1 AND d.endDate > ?1")
    List<Discount> getByInTimeDiscount(Instant now);

    @Query("SELECT d FROM Discount d WHERE d.startDate < ?1 AND d.endDate > ?1 AND d.isForProduct = false ")
    Set<Discount> getByInTimeDiscountAndForBill(Instant now);
}
