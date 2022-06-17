package com.mindhub.HomeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private boolean active;
    private boolean expired;
    private String cardholder;

    private CardType type;

    private ColorCard color;

    private String number;

    private int cvv;

    private LocalDateTime fromDate;

    private LocalDateTime thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;


    public Card() {
    }

    public Card(String cardholder, CardType type, ColorCard color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, Client client) {
        this.active = true;
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.expired = this.isExpired();
        this.client = client;
    }

    public Card(String cardholder, CardType type, ColorCard color, String number, int cvv, Client client) {
        this.active = true;
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = LocalDateTime.now();
        this.thruDate = LocalDateTime.now().plusYears(5);
        this.expired = this.isExpired();
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isExpired() {
        if (0 > this.thruDate.compareTo(LocalDateTime.now())) {
            return this.expired = true;
        }
        return this.expired = false;

    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public ColorCard getColor() {
        return color;
    }

    public void setColor(ColorCard color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
