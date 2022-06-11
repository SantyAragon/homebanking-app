package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface CardService {
    Set<Card> getAllCardsAuthenticated(Authentication authentication);
    void saveCard(Card card);

}
