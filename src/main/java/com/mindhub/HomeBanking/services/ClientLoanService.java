package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.ClientLoanDTO;
import com.mindhub.HomeBanking.models.ClientLoan;

import java.util.Set;

public interface ClientLoanService {
    Set<ClientLoanDTO> getAllClientsLoans();

    void saveClientLoan(ClientLoan clientLoan);
}
