package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.ClientLoanDTO;
import com.mindhub.HomeBanking.dtos.LoanApplicationDTO;
import com.mindhub.HomeBanking.dtos.LoanDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @RequestMapping("/clientsloans")
    public List<ClientLoanDTO> getClientsLoans() {
        return clientLoanRepository.findAll().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
    }

    @RequestMapping("/loans")
    public Set<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        Double amount = loanApplication.getAmount();
        Integer payment = loanApplication.getPayment();
        Loan loan = loanRepository.findById(loanApplication.getId()).orElse(null);

        Set<Loan> loansTaked = client.getLoans().stream().map(clientLoanDTO -> loanRepository.findById(clientLoanDTO.getIdLoan()).orElse(null)).collect(Collectors.toSet());
        Account targetAccount = accountRepository.findByNumber(loanApplication.getTargetAccount());

        //IF AMOUNT OR PAYMENT ITS EMPTY
        if (amount == 0 || amount.toString().isEmpty() || payment == 0 || payment.toString().isEmpty() || loanApplication.getTargetAccount().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //IF AMOUNT ITS NEGATIVE (!)
        if (amount <= 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);

        }
        //IF THE LOAN NOT EXIST
        if (!loanRepository.findAll().contains(loan)) {
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

        ClientLoan clientLoan = new ClientLoan(amount * 1.2, payment, client, loan);
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, amount, loan.getName() + " loan approved", LocalDateTime.now(), targetAccount);
        transactionRepository.save(transaction);

        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountRepository.save(targetAccount);

        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);

    }
}
