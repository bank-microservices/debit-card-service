package com.nttdata.microservices.debitcard.service;

import com.nttdata.microservices.debitcard.service.dto.DebitCardDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {

  Flux<DebitCardDto> findAll();

  Mono<DebitCardDto> findById(String id);

  Mono<DebitCardDto> findByCardNumber(String cardNumber);

  Mono<DebitCardDto> create(DebitCardDto debitCardDto);

  Mono<DebitCardDto> update(String id, DebitCardDto debitCardDto);

  Mono<Void> delete(String id);

}
