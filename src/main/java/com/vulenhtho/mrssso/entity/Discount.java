package com.vulenhtho.mrssso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Long percent;

    @Column
    private Long amount;

    @Column
    private String content;

    @Column
    private Instant startDate;

    @Column
    private Instant endDate;

    @Column
    private Boolean isForProduct;

    @ManyToMany(mappedBy = "discounts")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

}
