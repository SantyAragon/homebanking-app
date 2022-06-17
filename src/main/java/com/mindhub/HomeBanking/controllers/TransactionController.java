package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.PaymentDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.CardService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            Authentication authentication, @RequestParam String description,
            @RequestParam Double amount, @RequestParam String originAccountNumber, @RequestParam String targetAccountNumber) {

        Client client = clientService.getClientCurrent(authentication);
        Account accountOrigin = accountService.getAccountByNumber(originAccountNumber);
        Account accountTarget = accountService.getAccountByNumber(targetAccountNumber);


        //IF MISSING ONE O MORE PARAMETERS
        if (description.isEmpty() || amount == null || amount.isNaN() || amount.isInfinite() || originAccountNumber.isEmpty() || targetAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //IF AMOUNT ITS NEGATIVE (!)
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        //IF ACCOUNTS PARAMETERS ITS EQUALS
        if (originAccountNumber.equals(targetAccountNumber)) {
            return new ResponseEntity<>("Invalid accounts", HttpStatus.FORBIDDEN);
        }
        //IF ACCOUNT ORIGIN NOT REGISTERED IN DATABASE
        if (accountOrigin == null) {
            return new ResponseEntity<>("Invalid origin account", HttpStatus.FORBIDDEN);
        }
        //IF TARGET ACCOUNT NOT REGISTERED IN DATABASE
        if (accountTarget == null) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
        // IF THE CLIENT AUTHENTICATED NOT OWNER OF THE ORIGIN ACCOUNT
        if (!client.getAccounts().contains(accountOrigin)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }

        //IF THE ORIGIN ACCOUNT NOT ENOUGH BALANCE
        if (accountOrigin.getBalance() < amount) {
            return new ResponseEntity<>("You don't have enough balance", HttpStatus.FORBIDDEN);
        }
        //IF THE ORIGIN ACCOUNT ITS EQUALS OF THE TARGET ACCOUNT
        if (accountOrigin == accountTarget) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount, "Transfer sent to " + targetAccountNumber + " - " + description, LocalDateTime.now(), accountOrigin);

        accountTarget.setBalance(accountTarget.getBalance() + amount);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, "Transfer received from " + originAccountNumber + " - " + description, LocalDateTime.now(), accountTarget);


        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);
        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountTarget);

        return new ResponseEntity<>("Transaction success", HttpStatus.CREATED);
    }

    //Además debes crear una aplicación de front end separada(con html y js - vue) que presente
//        una interfaz de punto de venta que haga el llamado al servicio e indique si la operación fue realizada o no, en
//        caso de error mostrar por qué ocurrió.
    @CrossOrigin
    @Transactional
    @PostMapping("/transactions/payment")
    public ResponseEntity<Object> registerPayment(@RequestBody PaymentDTO paymentDTO) {
        int cvv = paymentDTO.getCvv();
        double amount = paymentDTO.getAmount();
        String description = paymentDTO.getDescription();

        if (cardService.getCardByNumber(paymentDTO.getNumberCard()) == null)
            return new ResponseEntity<>("Invalid Card.", HttpStatus.FORBIDDEN);

        Card card = cardService.getCardByNumber(paymentDTO.getNumberCard());
        Client client = card.getClient();
        Account account = client.getAccounts().stream().filter(acc -> acc.getBalance() >= amount && acc.isActive()).findFirst().orElse(null);

        if (Integer.toString(cvv).isEmpty())
            return new ResponseEntity<>("Invalid CVV.", HttpStatus.FORBIDDEN);

        if (amount <= 0)
            return new ResponseEntity<>("Invalid amount.", HttpStatus.FORBIDDEN);

        if (description.isEmpty())
            return new ResponseEntity<>("Invalid description.", HttpStatus.FORBIDDEN);

//        if (!client.getCards().contains(card))
//            return new ResponseEntity<>("You are not the card owner.", HttpStatus.FORBIDDEN);

        if (card.isExpired())
            return new ResponseEntity<>("The card is expired.", HttpStatus.FORBIDDEN);

        if (!card.isActive())
            return new ResponseEntity<>("The card is disabled.", HttpStatus.FORBIDDEN);

        if (card.getCvv() != cvv)
            return new ResponseEntity<>("Incorrect CVV.", HttpStatus.FORBIDDEN);

        if (account == null)
            return new ResponseEntity<>("None of your accounts has a sufficient balance", HttpStatus.FORBIDDEN);

        if (account.getBalance() < amount)
            return new ResponseEntity<>("The account not enough balance.", HttpStatus.FORBIDDEN);

        if (!account.isActive())
            return new ResponseEntity<>("Your account is disabled.", HttpStatus.FORBIDDEN);


        Transaction transaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), account);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() - amount);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Payment approved", HttpStatus.ACCEPTED);
    }
}
