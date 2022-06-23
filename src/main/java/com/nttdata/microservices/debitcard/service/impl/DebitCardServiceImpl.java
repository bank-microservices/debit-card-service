package com.nttdata.microservices.debitcard.service.impl;

import static com.nttdata.microservices.debitcard.util.MessageUtils.getMsg;

import com.nttdata.microservices.debitcard.entity.account.Account;
import com.nttdata.microservices.debitcard.exception.BadRequestException;
import com.nttdata.microservices.debitcard.exception.ClientException;
import com.nttdata.microservices.debitcard.proxy.AccountProxy;
import com.nttdata.microservices.debitcard.proxy.ClientProxy;
import com.nttdata.microservices.debitcard.repository.DebitCardRepository;
import com.nttdata.microservices.debitcard.service.DebitCardService;
import com.nttdata.microservices.debitcard.service.dto.DebitCardDto;
import com.nttdata.microservices.debitcard.service.mapper.DebitCardMapper;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Slf4j
@RequiredArgsConstructor
@Service
public class DebitCardServiceImpl implements DebitCardService {

  private final DebitCardRepository debitRepository;
  private final DebitCardMapper debitMapper;
  private final AccountProxy accountProxy;
  private final ClientProxy clientProxy;

  @Override
  public Flux<DebitCardDto> findAll() {
    return debitRepository.findAll(true)
        .map(debitMapper::toDto);
  }

  @Override
  public Mono<DebitCardDto> findById(String id) {
    return debitRepository.findById(id)
        .map(debitMapper::toDto);
  }

  @Override
  public Mono<DebitCardDto> findByCardNumber(String cardNumber) {
    return debitRepository.findByCardNumber(cardNumber)
        .map(debitMapper::toDto);
  }

  @Override
  public Mono<DebitCardDto> create(DebitCardDto debitCardDto) {
    return Mono.just(debitCardDto)
        .flatMap(this::existDebitCard)
        .flatMap(this::existClient)
        .flatMap(this::validateAssociatedAccounts)
        .map(debitMapper::toEntity)
        .flatMap(debitRepository::save)
        .map(debitMapper::toDto)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<DebitCardDto> existDebitCard(DebitCardDto debitCardDto) {
    log.debug("find Debit Card by cardNumber: {}", debitCardDto.getCardNumber());

    return findByCardNumber(debitCardDto.getCardNumber())
        .flatMap(r -> Mono.error(new BadRequestException(getMsg("debit.card.already",
            debitCardDto.getCardNumber()))))
        .thenReturn(debitCardDto);
  }

  /**
   * If the client is not found, throw an exception, otherwise, set the client and
   * return the debitCardDto
   *
   * @param debitCardDto is the object that I'm passing to the method
   * @return A Mono of AccountDto
   */
  private Mono<DebitCardDto> existClient(DebitCardDto debitCardDto) {
    String documentNumber = debitCardDto.getClientDocumentNumber();
    log.debug("Request to proxy Client by documentNumber: {}", documentNumber);
    return clientProxy.getClientByDocumentNumber(documentNumber)
        .switchIfEmpty(Mono.error(new ClientException(getMsg("client.not.found"))))
        .doOnNext(debitCardDto::setClient)
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
        .thenReturn(debitCardDto);
  }

  private Mono<DebitCardDto> validateAssociatedAccounts(DebitCardDto debitCardDto) {
    return Mono.just(debitCardDto)
        .flatMap(
            dto -> existDebitCardAccount(dto.getMainAccountNumber(), dto.getClientDocumentNumber())
                .doOnNext(dto::setMainAccount)
                .thenReturn(dto))
        .flatMap(dto -> {
          final List<String> accountNumbers = dto.getSecondaryAccountNumber();
          return Flux.fromIterable(accountNumbers)
              .flatMap(number -> existDebitCardAccount(number, dto.getClientDocumentNumber()))
              .collect(Collectors.toList())
              .doOnNext(dto::setSecondaryAccounts)
              .thenReturn(dto);
        });
  }

  private Mono<Account> existDebitCardAccount(String accountNumber, String documentNumber) {
    final ClientException clientException =
        new ClientException(getMsg("account.number.not.available", accountNumber));

    return this.accountProxy.findByAccountNumberAndClientDocument(accountNumber, documentNumber)
        .switchIfEmpty(Mono.error(clientException))
        .single();
  }

  @Override
  public Mono<DebitCardDto> update(String id, DebitCardDto debitCardDto) {
    return null;
  }

  @Override
  public Mono<Void> delete(String id) {
    return debitRepository.findById(id)
        .map(debitCard -> {
          debitCard.setStatus(false);
          return debitCard;
        })
        .flatMap(debitRepository::save)
        .then();
  }
}
