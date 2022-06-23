package com.nttdata.microservices.debitcard.proxy;

import com.nttdata.microservices.debitcard.entity.account.Account;
import reactor.core.publisher.Mono;

public interface AccountProxy {

  Mono<Account> findByAccountNumberAndClientDocument(String accountNumber,
                                                     String documentNumber);

}
