package com.vulenhtho.mrssso.entity;

import com.vulenhtho.mrssso.entity.enumeration.PaymentType;
import com.vulenhtho.mrssso.entity.enumeration.ReceiptStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "receipt")
@Getter
@Setter
public class Receipt extends AbstractAuditing{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String customerName;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private Long shippingCosts;

    @Column
    private Long amount;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column
    private String paymentInfo;

    @Column
    private String  note;

    @Column
    private String security;

    @Column
    private Long coinsUsed;

    @Column
    @Enumerated(EnumType.STRING)
    private ReceiptStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "receipt")
    private Set<Item> items = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "receipt_discount",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discounts = new HashSet<>();
}
