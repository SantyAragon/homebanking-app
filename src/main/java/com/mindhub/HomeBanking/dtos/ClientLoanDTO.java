package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.ClientLoan;
import com.mindhub.HomeBanking.models.LoanType;

public class ClientLoanDTO {

    private long id;
    private long idLoan;
    private String name;
    private Integer payments;
    private double amount;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPayment();
        this.amount = clientLoan.getAmount();
    }

    public long getId() {
        return id;
    }

    public Integer getPayments() {
        return payments;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public long getIdLoan() {
        return idLoan;
    }
}
