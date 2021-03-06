package com.mindhub.HomeBanking.controllers;


import com.mindhub.HomeBanking.dtos.ClientDTO;

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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.HomeBanking.Utils.utils.randomNumber;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClientDTOById(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password) {
//@RequestBody te permite recibir un objeto ej: (@RequestBody Client client)

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.getClientByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        Account account = new Account(AccountType.SAVINGS, "VIN" + randomNumber(0, 99999999), LocalDateTime.now(), 0, client);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/admin")
    public ResponseEntity<Object> isAdmin(Authentication authentication) {
        if (!authentication.getName().contains("@admin.com")) {
            return new ResponseEntity<>("is client", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("is admin", HttpStatus.ACCEPTED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication) {
        return new ClientDTO(clientService.getClientCurrent(authentication));
    }

    @GetMapping("/authenticated")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {
        if (authentication != null)
            return new ResponseEntity<>("authenticated", HttpStatus.ACCEPTED);

        return new ResponseEntity<>("not authenticated", HttpStatus.ACCEPTED);
    }

    @GetMapping("/clients/current/verification")
    public ResponseEntity<ClientDTO> verificationToken(@RequestParam String token) {
        ClientDTO client = new ClientDTO(clientService.getClientByToken(token));

        return new ResponseEntity<>(client, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/clients/current/password")
    public ResponseEntity<?> changePassword(String newPassword, String email) {
        Client client = clientService.getClientByEmail(email);
        client.setPassword(passwordEncoder.encode(newPassword));
        clientService.saveClient(client);

        return new ResponseEntity<>("Change password success", HttpStatus.ACCEPTED);
    }

}
