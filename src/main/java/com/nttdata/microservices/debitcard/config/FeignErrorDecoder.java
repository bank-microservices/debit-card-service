package com.nttdata.microservices.debitcard.config;

import static com.nttdata.microservices.debitcard.util.MessageUtils.getMsg;

import com.nttdata.microservices.debitcard.exception.AccountException;
import com.nttdata.microservices.debitcard.exception.ClientException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder defaultErrorDecoder = new Default();

  @Override
  public Exception decode(String methodKey, Response response) {
    Exception exception = defaultErrorDecoder.decode(methodKey, response);

    log.error("Error accessing the service through the feign client. url: {} ",
        response.request().url(),
        exception);
    if (response.status() == HttpStatus.NOT_FOUND.value()) {
      if (methodKey.contains("findByAccountNumberAndClientDocument")) {
        return new AccountException(getMsg("account.not.found"), HttpStatus.NOT_FOUND.value());
      } else if (methodKey.contains("getClientByDocumentNumber")) {
        return new ClientException(getMsg("account.not.found"), HttpStatus.NOT_FOUND.value());
      }
    } else if (response.status() == 503 || response.status() == 504) {
      return new RetryableException(response.status(), "Service is unavailable",
          response.request().httpMethod(),
          null,
          response.request()
      );
    }
    return exception;
  }
}