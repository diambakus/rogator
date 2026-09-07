package com.orakuma.rogator.payment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
  default PaymentType mapToDomain(PaymentTypeRequest paymentTypeRequest) {
    return switch (paymentTypeRequest) {
      case PAYPAL -> PaymentType.PAYPAL;
    };
  }
}
