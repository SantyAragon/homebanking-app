package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomeBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
        return (args) -> {
            // save a couple of customers

            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
            clientRepository.save(client1);


            Account cuenta1 = new Account("VIN001", LocalDateTime.now(), 5000, client1);
            Account cuenta2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, client1);
            accountRepository.save(cuenta1);
            accountRepository.save(cuenta2);


            Client client3 = new Client("Santi", "aragon", "SantyA@mindhub.com");
            clientRepository.save(client3);
            Account cuenta3 = new Account("VIN003", LocalDateTime.now(), 10000, client3);
            accountRepository.save(cuenta3);

            Client client4 = new Client("Ivan", "gomez", "IvanG@mindhub.com");
            Client client5 = new Client("Juan", "perez", "JuanP@mindhub.com");
            Client client6 = new Client("Maria", "gonzalez", "MariaG@mindhub.com");


            clientRepository.save(client4);
            clientRepository.save(client5);
            clientRepository.save(client6);
            System.out.println("todo listo, arranca nomas rey");
        };

    }

}
