package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.Loan;

public class LoanApplicationDTO {

    private long id;
    private Double amount;
    private Integer payment;
    private String targetAccount;



    public LoanApplicationDTO(long id, Double amount, Integer payment, String targetAccount) {
        this.id = id;
        this.amount = amount;
        this.payment = payment;
        this.targetAccount = targetAccount;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public String getTargetAccount() {
        return targetAccount;
    }
}
