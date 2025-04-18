package com.orakuma.rogator.application;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "applications")
public class ApplicationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_gen")
    @SequenceGenerator(name ="app_gen", sequenceName = "app_seq", allocationSize = 1)
    @ToString.Exclude
    private Long          id;
    private String        name;
    private String        email;
    private BigDecimal    price;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private LocalDateTime created;
}
