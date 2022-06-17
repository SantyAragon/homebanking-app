package com.mindhub.HomeBanking.services.implement;

import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.CardRepository;
import com.mindhub.HomeBanking.services.CardService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<Card> getAllCardsAuthenticated(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        return cardRepository.findAll().stream().filter(card -> card.getClient() == client).collect(Collectors.toSet());
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number).orElse(null);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
