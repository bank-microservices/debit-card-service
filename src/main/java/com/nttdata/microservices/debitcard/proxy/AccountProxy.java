package com.nttdata.microservices.debitcard.proxy;

import com.nttdata.microservices.debitcard.entity.account.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient(value = "account-service", url = "${service.account.uri}")
public interface AccountProxy {

  @GetMapping("/number/{accountNumber}/client/{documentNumber}")
  Mono<Account> findByAccountNumberAndClientDocument(
      @PathVariable("accountNumber") String accountNumber,
      @PathVariable("documentNumber") String documentNumber);

}
