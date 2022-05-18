package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.CardType;
import com.mindhub.HomeBanking.models.ColorCard;

import java.time.LocalDateTime;

public class CardDTO {

    private long id;

    private String cardholder;

    private CardType type;

    private ColorCard color;

    private String number;

    private int cvv;

    private LocalDateTime fromDate;

    private LocalDateTime thruDate;


    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public ColorCard getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }
}
