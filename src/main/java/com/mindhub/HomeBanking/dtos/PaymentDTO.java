package com.mindhub.HomeBanking.dtos;

public class PaymentDTO {
    private String numberCard;
    private int cvv;
    private double amount;
    private String description;


    public PaymentDTO() {
    }

    public PaymentDTO(String numberCard, int cvv, double amount, String description) {
        this.numberCard = numberCard;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
