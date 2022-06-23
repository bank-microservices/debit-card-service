package com.nttdata.microservices.debitcard.service.dto;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nttdata.microservices.debitcard.entity.account.Account;
import com.nttdata.microservices.debitcard.entity.client.Client;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@NoArgsConstructor
public class DebitCardDto {

  @NotBlank(message = "clientDocumentNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String clientDocumentNumber;

  @NotBlank(message = "cardNumber is required")
  private String cardNumber;

  @NotBlank(message = "cvv is required")
  private String cvv;

  @NotNull(message = "expirationDate is required")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expirationDate;

  @NotBlank(message = "accountNumber is required")
  @JsonProperty(access = WRITE_ONLY)
  private String mainAccountNumber;

  @JsonProperty(access = WRITE_ONLY)
  private List<String> secondaryAccountNumber;

  @JsonProperty(access = READ_ONLY)
  private String id;
  @JsonProperty(access = READ_ONLY)
  private Client client;
  @JsonProperty(access = READ_ONLY)
  private Account mainAccount;
  @JsonProperty(access = READ_ONLY)
  private List<Account> secondaryAccounts;
  @JsonProperty(access = READ_ONLY)
  private boolean status = true;

}
