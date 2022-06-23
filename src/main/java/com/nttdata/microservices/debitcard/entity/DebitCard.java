package com.nttdata.microservices.debitcard.entity;

import com.nttdata.microservices.debitcard.entity.account.Account;
import com.nttdata.microservices.debitcard.entity.client.Client;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "debitCard")
public class DebitCard extends AbstractAuditingEntity{

  @Id
  private String id;
  private String cardNumber;
  private String cvv;
  private String expirationDate;
  private Client client;
  private Account mainAccount;
  private List<Account> secondaryAccounts;
  private boolean status = true;

}
