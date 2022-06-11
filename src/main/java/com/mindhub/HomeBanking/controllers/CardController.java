package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.CardDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.services.CardService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.HomeBanking.Utils.utils.randomNumber;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam ColorCard colorCard, @RequestParam CardType cardType) {

        Client client = clientService.getClientCurrent(authentication);
        Set<Card> cardsClient = cardService.getAllCardsAuthenticated(authentication);
        Set<Card> cardsDebit = cardsClient.stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());
        Set<Card> cardsCredit = cardsClient.stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toSet());

        if (cardsDebit.size() < 3 && cardType == CardType.DEBIT) {
            Card card = new Card(client.getFullName(), CardType.DEBIT, colorCard, "4517" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999), randomNumber(100, 999), client);
            cardService.saveCard(card);
            return new ResponseEntity<>("Created success", HttpStatus.CREATED);
        } else if (cardsCredit.size() < 3 && cardType == CardType.CREDIT) {
            Card card = new Card(client.getFullName(), CardType.CREDIT, colorCard, "4509" + randomNumber(1000, 9999) + randomNumber(1000, 9999) + randomNumber(1000, 9999), randomNumber(100, 999), client);
            cardService.saveCard(card);
            return new ResponseEntity<>("Created success", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Limit cards reached", HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

//    @RequestMapping("/card/delete")
//    public ResponseEntity<Object> deleteCard(Authentication authentication){
//
//        cardRepository.delete(card);
//    }


}
