package com.orakuma.rogator.application_form;

import com.orakuma.rogator.application.ApplicationEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "application_forms", schema = "rogator")
public class ApplicationFormEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_form_gen")
    @SequenceGenerator(name ="app_form_gen", sequenceName = "app_form_seq", allocationSize = 1)
    @ToString.Exclude
    private Long              id;
    private int               position;
    private String            name;
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity application;
    @Column(columnDefinition = "jsonb")
    private String            content;
    private LocalDate         created;
}
