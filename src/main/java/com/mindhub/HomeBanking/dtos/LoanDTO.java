package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.ClientLoan;
import com.mindhub.HomeBanking.models.Loan;
import com.mindhub.HomeBanking.models.LoanType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanDTO {
    private long id;

    private int percentIncrease;
    private LoanType name;

    private int maxAmount;

    private List<Integer> payments = new ArrayList<>();

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.percentIncrease= loan.getPercentIncrease();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public long getId() {
        return id;
    }

    public int getPercentIncrease() {return percentIncrease;}

    public LoanType getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
