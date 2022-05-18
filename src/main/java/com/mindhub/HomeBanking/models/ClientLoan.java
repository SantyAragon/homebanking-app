package com.mindhub.HomeBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.stream.Collectors;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int amount;
    private Integer payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client clientLoan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;


    public ClientLoan() {
    }

    public ClientLoan(int amount, Integer payment, Client clientLoan, Loan loan) {
        this.amount = amount;
        this.payment = payment;
        this.clientLoan = clientLoan;
        this.loan = loan;
    }

    public long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public Client getClient() {
        return clientLoan;
    }

    public void setClient(Client clientLoan) {
        this.clientLoan = clientLoan;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
