package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.HomeBanking.Utils.utils.randomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAll() {
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountsCurrent(Authentication authentication) {
        Set<Account> accountsClientCurrent = accountService.getAllAccountsAuthenticated(authentication);

        return accountsClientCurrent.stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccountCurrent(Authentication authentication, @PathVariable Long id) {

        Set<Account> collectionAccount = accountService.getAllAccountsAuthenticated(authentication);

        return collectionAccount.stream().filter(account -> account.getId() == id).map(account -> new AccountDTO(account)).findFirst().orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createNewAccount(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        Set<Account> accounts = accountService.getAllAccountsAuthenticated(authentication);

        if (accounts.size() < 3) {
            accountService.saveAccount(new Account("VIN-" + randomNumber(1000000, 99999999), LocalDateTime.now(), 0, client));
            return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }

    }

}
