package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.models.TransactionType;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
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
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
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

            Transaction transaction1 = new Transaction(TransactionType.CREDITO,550,"Transfer received from Rune Bank ",LocalDateTime.now(),cuenta1);
            transactionRepository.save(transaction1);
            Transaction transaction2 = new Transaction(TransactionType.DEBITO,1099.99,"Riot Games 2500 RP",LocalDateTime.now(),cuenta1);
            transactionRepository.save(transaction2);
            Transaction transaction4 = new Transaction(TransactionType.DEBITO,3099.99,"Riot Games 5500 RP",LocalDateTime.now(),cuenta1);
            transactionRepository.save(transaction4);

            Transaction transaction3 = new Transaction(TransactionType.CREDITO,9900,"Moonton salary",LocalDateTime.now(),cuenta2);
            transactionRepository.save(transaction3);
            Transaction transaction5 = new Transaction(TransactionType.DEBITO,3999,"Steam store",LocalDateTime.now(),cuenta2);
            transactionRepository.save(transaction5);
            Transaction transaction6 = new Transaction(TransactionType.DEBITO,1650,"Mc Donalds",LocalDateTime.now(),cuenta2);
            transactionRepository.save(transaction6);
            Transaction transaction7 = new Transaction(TransactionType.DEBITO,5300,"Supermarket",LocalDateTime.now(),cuenta2);
            transactionRepository.save(transaction7);


            clientRepository.save(client4);
            clientRepository.save(client5);
            clientRepository.save(client6);
            System.out.println("todo listo, arranca nomas rey");
        };

    }

}
