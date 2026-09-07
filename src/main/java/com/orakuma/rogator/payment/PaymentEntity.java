package com.orakuma.rogator.payment;

import com.orakuma.rogator.application.ApplicationEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "payments", schema = "rogator")
@Accessors(chain = true)
public class PaymentEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @EqualsAndHashCode.Include private Long id;

  @Column(name = "order_id", nullable = false, unique = true)
  private String orderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type")
  private PaymentType paymentType;

  private LocalDateTime created;

  private LocalDateTime updated;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "application_id", updatable = false)
  @ToString.Exclude
  private ApplicationEntity application;
}