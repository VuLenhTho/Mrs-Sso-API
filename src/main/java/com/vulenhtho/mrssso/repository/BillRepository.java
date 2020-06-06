package com.vulenhtho.mrssso.repository;

import com.vulenhtho.mrssso.entity.Bill;
import com.vulenhtho.mrssso.entity.enumeration.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

    @Query("SELECT b FROM Bill b WHERE b.lastModifiedDate > ?1 AND b.lastModifiedDate < ?2 AND b.status = ?3")
    List<Bill> getByLastModifiedDateAndStatus(Instant startDate, Instant endDate, BillStatus billStatus);
}
