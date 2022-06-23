package com.nttdata.microservices.debitcard.repository;

import com.nttdata.microservices.debitcard.entity.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {

  Mono<DebitCard> findByCardNumber(String cardNumber);

}
