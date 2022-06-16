package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.ClientLoan;
import com.mindhub.HomeBanking.models.Loan;
import com.mindhub.HomeBanking.models.LoanType;

import java.util.ArrayList;
import java.util.List;


public class LoanDTO {
    private long id;

    private int percentIncrease;
    private String name;

    private int maxAmount;

    private List<Integer> payments = new ArrayList<>();

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.percentIncrease = loan.getPercentIncrease();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public long getId() {
        return id;
    }

    public int getPercentIncrease() {
        return percentIncrease;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
