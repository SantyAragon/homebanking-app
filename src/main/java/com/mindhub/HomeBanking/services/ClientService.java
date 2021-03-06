package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.ClientDTO;
import com.mindhub.HomeBanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClients();

    ClientDTO getClientDTOById(Long id);

    Client getClientById(Long id);

    Client getClientCurrent(Authentication authentication);

    Client getClientByEmail(String email);

    void saveClient(Client client);

    Client getClientByToken(String token);
}
