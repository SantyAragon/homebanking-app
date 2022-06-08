package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.models.TransactionType;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            Authentication authentication, @RequestParam String description,
            @RequestParam Double amount, @RequestParam String originAccount, @RequestParam String targetAccount) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Account accountOne = accountRepository.findByNumber(originAccount);
        Account accountTwo = accountRepository.findByNumber(targetAccount);


        //IF MISSING ONE O MORE PARAMETERS
        if (description.isEmpty() || amount == null || amount.isNaN() || amount.isInfinite() || originAccount.isEmpty() || targetAccount.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //IF AMOUNT ITS NEGATIVE (!)
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        //IF ACCOUNTS PARAMETERS ITS EQUALS
        if (originAccount.equals(targetAccount)) {
            return new ResponseEntity<>("Invalid accounts", HttpStatus.FORBIDDEN);
        }
        //IF ACCOUNT ORIGIN NOT REGISTERED IN DATABASE
        if (accountRepository.findByNumber(originAccount) == null) {
            return new ResponseEntity<>("Invalid origin account", HttpStatus.FORBIDDEN);
        }
        //IF TARGET ACCOUNT NOT REGISTERED IN DATABASE
        if (accountRepository.findByNumber(targetAccount) == null) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
        // IF THE CLIENT AUTHENTICATED NOT OWNER OF THE ORIGIN ACCOUNT
        if (!client.getAccounts().contains(accountOne)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }

        //IF THE ORIGIN ACCOUNT NOT ENOUGH BALANCE
        if (accountOne.getBalance() < amount) {
            return new ResponseEntity<>("You don't have enough balance", HttpStatus.FORBIDDEN);
        }
        //IF THE ORIGIN ACCOUNT ITS EQUALS OF THE TARGET ACCOUNT
        if (accountOne == accountTwo) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
        accountOne.setBalance(accountOne.getBalance() - amount);
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount, "Transfer sent to " + targetAccount + " - " + description, LocalDateTime.now(), accountOne);

        accountTwo.setBalance(accountTwo.getBalance() + amount);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, "Transfer received from " + originAccount + " - " + description, LocalDateTime.now(), accountTwo);


        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        accountRepository.save(accountOne);
        accountRepository.save(accountTwo);

        return new ResponseEntity<>("Transaction success", HttpStatus.CREATED);
    }


}
