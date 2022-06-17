package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;

public interface CardService {
    Set<Card> getAllCardsAuthenticated(Authentication authentication);

    Card getCardByNumber(String number);

    void saveCard(Card card);

}
