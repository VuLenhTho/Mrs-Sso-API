package com.vulenhtho.mrssso.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditing implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column( nullable = false, length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column( updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column
    private Instant lastModifiedDate = Instant.now();


}
