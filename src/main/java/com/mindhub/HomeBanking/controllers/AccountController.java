package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.dtos.ClientDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAll() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping("/accounts/current")
    public Set<AccountDTO> getAccountsCurrent(Authentication authentication) {
        Long idClient = clientRepository.findByEmail(authentication.getName()).getId();
        Stream<Account> collectionAccount = accountRepository.findAll().stream().filter(account -> account.getClient().getId() == idClient);

        return collectionAccount.map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @RequestMapping("/accounts/current/{id}")
    public AccountDTO getAccountCurrent(Authentication authentication, @PathVariable Long id) {
        Long idClient = clientRepository.findByEmail(authentication.getName()).getId();
        Stream<Account> collectionAccount = accountRepository.findAll().stream().filter(account -> account.getClient().getId() == idClient);

        return collectionAccount.filter(account -> account.getId() == id).map(account -> new AccountDTO(account)).findFirst().orElse(null);
    }

}
