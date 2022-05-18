package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.ClientLoanDTO;
import com.mindhub.HomeBanking.models.Loan;
import com.mindhub.HomeBanking.repositories.ClientLoanRepository;
import com.mindhub.HomeBanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class LoanController {

    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private LoanRepository loanRepository;


    @RequestMapping("/clientsloans")
    public List<ClientLoanDTO> getClientsLoans() {
        return clientLoanRepository.findAll().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
    }

    @RequestMapping("/loans")
    public List<Loan> getLoans() {
        return loanRepository.findAll();
    }

}
