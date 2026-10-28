package com.orakuma.rogator.payment.pispi;

import com.orakuma.rogator.config.payment.pispi.PispiProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PispiPaymentMapper {
  @Mapping(target = "montant", source = "pispiAndApplicationInput.price")
  @Mapping(target = "motif", source = "pispiAndApplicationInput.serviceTitle")
  @Mapping(target = "txId", source = "pispiAndApplicationInput.applicationCode")
  @Mapping(target = "payeurAlias", source = "pispiAndApplicationInput.payeurAlias")
  @Mapping(target = "payeAlias", source = "pispiProperties.payeAlias")
  @Mapping(target = "logoUrl", source = "pispiProperties.logoUrl")
  @Mapping(target = "categorie", source = "pispiProperties.categorie")
  @Mapping(target = "confirmation", source = "confirmation")
  @Mapping(target = "dateLimitePaiement", source = "dateLimitePaiement")
  CreatePaymentRequestPayload map(
      PispiAndApplicationInput pispiAndApplicationInput,
      PispiProperties pispiProperties,
      boolean confirmation,
      LocalDateTime dateLimitePaiement);
}
