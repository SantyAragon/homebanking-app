package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.models.TransactionType;
import com.mindhub.HomeBanking.services.AccountService;
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


}
