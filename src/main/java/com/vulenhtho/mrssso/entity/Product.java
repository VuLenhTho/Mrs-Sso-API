package com.vulenhtho.mrssso.entity;

import com.vulenhtho.mrssso.entity.enumeration.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column
    private Long importPrice;

    @Column
    private Long price;

    @Column
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductStatus status;

    @Column
    private String thumbnail;

    @Column
    private String photoList;

    @Column
    private Boolean hot;

    @Column
    private Boolean trend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product")
    private Set<ProductColorSize> productColorSizes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private Set<Color> colors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_discount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discounts = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_size",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id"))
    private Set<com.vulenhtho.mrssso.entity.Size> sizes = new HashSet<>();

}
