package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.AccountType;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/accounts")
    public List<AccountDTO> getAll() {
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountDTOById(id);
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
    public ResponseEntity<Object> createNewAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client client = clientService.getClientCurrent(authentication);
        Set<Account> accounts = accountService.getAllAccountsAuthenticated(authentication).stream().filter(account -> account.isActive()).collect(Collectors.toSet());

        if (!accountType.equals(AccountType.SAVINGS) && !accountType.equals(AccountType.CHECKING)) {
            return new ResponseEntity<>("Missing type account", HttpStatus.FORBIDDEN);
        }
        if (accounts.size() >= 3) {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(accountType, "VIN" + randomNumber(1000000, 99999999), LocalDateTime.now(), 0, client);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    }


    @Transactional
    @PatchMapping("/clients/current/accounts/disabled")
    public ResponseEntity<Object> disableAccount(Authentication authentication, @RequestParam Long idAccount, @RequestParam String password) {
        Client client = clientService.getClientCurrent(authentication);
        Account accountDisable = accountService.getAccountById(idAccount);

        if (idAccount.toString().isEmpty()) {
            return new ResponseEntity<>("Missing ID account", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(password, client.getPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.FORBIDDEN);
        }
        if (idAccount <= 0) {
            return new ResponseEntity<>("Invalid ID account", HttpStatus.FORBIDDEN);
        }
        if (accountDisable == null) {
            return new ResponseEntity<>("Invalid Account", HttpStatus.FORBIDDEN);
        }
        if (!accountDisable.isActive()) {
            return new ResponseEntity<>("Account already deactivated", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountDisable)) {
            return new ResponseEntity<>("You are not the account owner", HttpStatus.FORBIDDEN);
        }
        if (accountDisable.getBalance() > 0) {
            return new ResponseEntity<>("Your account has balance available, please transfer these balance", HttpStatus.FORBIDDEN);
        }

        accountDisable.setActive(false);
        accountService.saveAccount(accountDisable);

        return new ResponseEntity<>("Account disabled successfully", HttpStatus.ACCEPTED);
    }
}
