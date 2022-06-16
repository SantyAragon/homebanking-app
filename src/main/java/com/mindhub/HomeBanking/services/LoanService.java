package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.LoanDTO;
import com.mindhub.HomeBanking.models.Loan;

import java.util.Set;

public interface LoanService {

    Set<LoanDTO> getAllLoansDTO();

    Set<Loan> getAllLoans();

    void saveLoan(Loan loan);

    Loan getLoanById(Long id);
}
