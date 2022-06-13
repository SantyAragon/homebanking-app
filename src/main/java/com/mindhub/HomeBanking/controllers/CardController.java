package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.CardDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.services.CardService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam ColorCard colorCard, @RequestParam CardType cardType) {

        Client client = clientService.getClientCurrent(authentication);
        Set<Card> cardsClient = cardService.getAllCardsAuthenticated(authentication);
        Set<Card> cardsDebit = cardsClient.stream().filter(card -> card.getType() == CardType.DEBIT && card.isActive()).collect(Collectors.toSet());
        Set<Card> cardsCredit = cardsClient.stream().filter(card -> card.getType() == CardType.CREDIT && card.isActive()).collect(Collectors.toSet());

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

    //RETURN ALL CARDS ACTIVES
    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCardsDTO(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    //RETURN ALL CARDS(ACTIVE AND DISABLED) OF CLIENT - ONLY ADMINS
    @GetMapping("/clients/cards/all")
    public Set<Card> getAllCards(@RequestParam Long idClient) {
        Client client = clientService.getClientById(idClient);
        return client.getCards();
    }

    //CHANGE THE PROPERTY STATUS(ACTIVE/DISABLED)
    @Transactional
    @PatchMapping("/clients/current/cards/disabled")
    public ResponseEntity<Object> disabledCard(Authentication authentication, @RequestParam Long idCard, @RequestParam String password) {
        Set<Card> cards = cardService.getAllCardsAuthenticated(authentication);
        Client client = clientService.getClientCurrent(authentication);

        Card cardDisabled = cards.stream().filter(card -> card.getId() == idCard).findFirst().orElse(null);

        System.out.println(client.getPassword());
        System.out.println(passwordEncoder.encode(password));
        if (!passwordEncoder.matches(password, client.getPassword())) {
            return new ResponseEntity<>("Password Incorrect", HttpStatus.FORBIDDEN);
        }

        if (idCard.toString().isEmpty()) {
            return new ResponseEntity<>("Missing Id Card", HttpStatus.FORBIDDEN);
        }
        if (cardDisabled == null) {
            return new ResponseEntity<>("Invalid card", HttpStatus.FORBIDDEN);
        }
        if (!cardDisabled.isActive()) {
            return new ResponseEntity<>("Card already disabled", HttpStatus.FORBIDDEN);
        }

        cardDisabled.setActive(false);
        cardService.saveCard(cardDisabled);
        return new ResponseEntity<>("Card successfully deactivated", HttpStatus.ACCEPTED);
    }


}
