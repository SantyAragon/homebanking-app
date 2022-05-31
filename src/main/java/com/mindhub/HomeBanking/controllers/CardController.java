package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.CardDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.CardRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.HomeBanking.Utils.utils.randomNumber;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;


    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam ColorCard colorCard, @RequestParam CardType cardType) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> cardsClient = cardRepository.findAll().stream().filter(card -> card.getClient() == client).collect(Collectors.toSet());
        Set<Card> cardsDebit = cardsClient.stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());
        Set<Card> cardsCredit = cardsClient.stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toSet());

        if (cardsDebit.size() < 3 && cardType == CardType.DEBIT) {
            Card card = new Card(client.getFullName(), CardType.DEBIT, colorCard, "4517" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999), randomNumber(100, 999), client);
            cardRepository.save(card);
            return new ResponseEntity<>("Created success", HttpStatus.CREATED);
        } else if (cardsCredit.size() < 3 && cardType == CardType.CREDIT) {
            Card card = new Card(client.getFullName(), CardType.CREDIT, colorCard, "4509" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999), randomNumber(100, 999), client);
            cardRepository.save(card);
            return new ResponseEntity<>("Created success", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Limit cards reached", HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }
}
