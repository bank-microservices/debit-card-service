package com.nttdata.microservices.debitcard.proxy;

import com.nttdata.microservices.debitcard.entity.client.Client;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient(value = "client-service", url = "${service.client.uri}")
public interface ClientProxy {

  @GetMapping("/document-number/{number}")
  Mono<Client> getClientByDocumentNumber(@PathVariable("number") String documentNumber);

}
