package com.nttdata.microservices.debitcard.proxy;

import com.nttdata.microservices.debitcard.entity.client.Client;
import reactor.core.publisher.Mono;

public interface ClientProxy {

  Mono<Client> getClientByDocumentNumber(String documentNumber);

}
