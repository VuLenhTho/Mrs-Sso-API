package com.vulenhtho.mrssso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user")
@Getter
@Setter
public class User extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false, length = 50)
    private String userName;

    @JsonIgnore
    @Size(min = 1, max = 60)
    @Column(length = 60, nullable = false)
    private String password;

    @Size(max = 60)
    @Column(length = 60)
    private String fullName;

    @Column
    private Boolean sex;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Size(min = 9, max = 15)
    @Column(unique = true, length = 15)
    private String phone;

    @Column
    private String address;

    @Column
    private String avatarUrl;

    @Column
    private Long coins;

    @Column(nullable = false)
    private Boolean activated;

    @Column(nullable = false)
    private Boolean locked;

    @Size(max = 20)
    @Column(length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(length = 20)
    @JsonIgnore
    private String resetKey;

    @Column
    private Instant resetDate;

    @ManyToMany
    @JoinTable(name = "permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Bill> bills = new HashSet<>();

}
