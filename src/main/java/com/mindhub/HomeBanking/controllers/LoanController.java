package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.ClientLoanDTO;
import com.mindhub.HomeBanking.dtos.LoanApplicationDTO;
import com.mindhub.HomeBanking.dtos.LoanDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class LoanController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/clientsloans")
    public Set<ClientLoanDTO> getClientsLoans() {
        return clientLoanService.getAllClientsLoans();
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getLoans() {
        return loanService.getAllLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication) {
        Client client = clientService.getClientCurrent(authentication);

        Double amount = loanApplication.getAmount();
        Integer payment = loanApplication.getPayment();
        String targetAccountNumber = loanApplication.getTargetAccount();
        Loan loan = loanService.getLoanById(loanApplication.getId());
        Set<Loan> allLoans = loanService.getAllLoans();


        Set<Loan> loansTaked = client.getLoans().stream().map(clientLoanDTO -> loanService.getLoanById(clientLoanDTO.getIdLoan())).collect(Collectors.toSet());
        Account targetAccount = accountService.getAccountByNumber(targetAccountNumber);

        //IF AMOUNT OR PAYMENT ITS EMPTY
        if (amount == 0 || amount.toString().isEmpty() || payment == 0 || payment.toString().isEmpty() || loanApplication.getTargetAccount().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //IF AMOUNT ITS NEGATIVE (!)
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        //IF THE LOAN NOT EXIST
        if (!allLoans.contains(loan)) {
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
        //IF THE CLIENT ALREADY TAKE THE LOAN REQUESTED

//        if (client.getLoans().stream().filter(clientLoanDTO -> clientLoanDTO.getIdLoan() == loan.getId()).collect(Collectors.toSet()).size() > 0) {
//            return new ResponseEntity<>("Loan already taked", HttpStatus.FORBIDDEN);
//        }
        if (loansTaked.contains(loan)) {
            return new ResponseEntity<>("Loan already taked", HttpStatus.FORBIDDEN);
        }

        //IF AMOUNT EXCEDED OF MAX AMOUNT
        if (amount > loan.getMaxAmount()) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        //IF PAYMENT NOT EXIST IN PAYMENTS
        if (!loan.getPayments().contains(payment)) {
            return new ResponseEntity<>("Invalid payment", HttpStatus.FORBIDDEN);
        }
        // IF TARGET ACCOUNT NOT EXIST IN DATABASE
        if (!client.getAccounts().contains(targetAccount)) {
            return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
        }
        // IF THE CLIENT AUTHENTICATED NOT OWNER OF THE TARGET ACCOUNT
        if (!client.getAccounts().contains(targetAccount)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(amount * (loan.getPercentIncrease() * 0.01 + 1), payment, client, loan);
        clientLoanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, amount, loan.getName() + " loan approved", LocalDateTime.now(), targetAccount);
        transactionService.saveTransaction(transaction);

        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountService.saveAccount(targetAccount);

        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(@RequestParam String nameLoan, @RequestParam int percentIncrease, @RequestParam int maxAmount, @RequestParam ArrayList<Integer> payments) {

        //IF THE LOAN NAME ITS EMPTY
        if (nameLoan.isEmpty()) {
            return new ResponseEntity<>("Invalid name loan", HttpStatus.FORBIDDEN);
        }
        //IF THE PERCENT INCREASE ITS NEGATIVE OR 0
        if (percentIncrease <= 0) {
            return new ResponseEntity<>("Invalid percent increase", HttpStatus.FORBIDDEN);
        }
        //IF THE MAX AMOUNT OF LOAN ITS NEGATIVE OR 0
        if (maxAmount <= 0) {
            return new ResponseEntity<>("Invalid max amount", HttpStatus.FORBIDDEN);
        }
//        if (payments.stream().filter(payment -> payment <= 0).count() > 0) {
//            return new ResponseEntity<>("One or more payments its invalid.", HttpStatus.FORBIDDEN);
//        }

        //IF ANY OF PAYMENTS ITS NEGATIVE OR 0
        if (payments.stream().anyMatch(payment -> payment <= 0)) {
            return new ResponseEntity<>("One or more payments its invalid.", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(nameLoan, maxAmount, payments, percentIncrease);
        loanService.saveLoan(loan);
        return new ResponseEntity<>("Create loan success", HttpStatus.ACCEPTED);

    }



}
