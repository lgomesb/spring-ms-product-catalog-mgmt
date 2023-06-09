package com.barbosa.ms.productmgmt.domain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    @NotEmpty(message = "Name is required")
    @Column(columnDefinition = "varchar(255) not null")
    private String name;
    
    @Column(columnDefinition = "varchar(1) not null default 'A'")
    private String status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by", columnDefinition = "varchar(100) not null default '99999'")
    private String createdBy;

    @Column(name = "modified_on")
    private LocalDateTime modifieldOn;

    @Column(name = "modified_by", columnDefinition = "varchar(100)")
    private String modifiedBy;

    @PrePersist
    public void prePersist() {
        setCreatedOn(LocalDateTime.now());
        setCreatedBy("99999");
        setStatus("A");
    }
    
}
