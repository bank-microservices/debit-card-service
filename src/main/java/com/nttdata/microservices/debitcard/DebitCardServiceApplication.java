package com.nttdata.microservices.debitcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DebitCardServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DebitCardServiceApplication.class, args);
  }

}
