package com.nttdata.microservices.debitcard.controller;

import com.nttdata.microservices.debitcard.service.DebitCardService;
import com.nttdata.microservices.debitcard.service.dto.DebitCardDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/debit-card")
@RequiredArgsConstructor
public class DebitCardController {

  private final DebitCardService debitCardService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  private Flux<DebitCardDto> findAll() {
    return debitCardService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<DebitCardDto>> findById(@PathVariable("id") String id) {
    return debitCardService.findById(id)
        .map(ResponseEntity::ok)
        .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
        .onErrorReturn(WebClientRequestException.class,
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
  }

  @GetMapping(value = "/card-number/{card-number}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<DebitCardDto>> findByCardNumber(
      @PathVariable("card-number") String cardNumber) {
    return debitCardService.findByCardNumber(cardNumber)
        .map(ResponseEntity::ok)
        .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
        .onErrorReturn(WebClientRequestException.class,
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<ResponseEntity<DebitCardDto>> create(@Valid @RequestBody DebitCardDto debitCardDto) {
    return debitCardService.create(debitCardDto)
        .map(ResponseEntity::ok)
        .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
        .onErrorReturn(WebClientRequestException.class,
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  private Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
    return debitCardService.delete(id)
        .map(ResponseEntity::ok)
        .onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
        .onErrorReturn(WebClientRequestException.class,
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
  }

}
