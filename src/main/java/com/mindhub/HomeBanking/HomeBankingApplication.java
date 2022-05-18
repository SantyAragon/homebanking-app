package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.mindhub.HomeBanking.models.CardType.*;
import static com.mindhub.HomeBanking.models.ColorCard.*;
import static com.mindhub.HomeBanking.models.LoanType.*;

@SpringBootApplication
public class HomeBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
        return (args) -> {
            // save a couple of customers

            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
            clientRepository.save(client1);


            Account cuenta1 = new Account("VIN001", LocalDateTime.now(), 5000, client1);
            Account cuenta2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, client1);
            Account cuenta4 = new Account("VIN004", LocalDateTime.now(), 6500, client1);
            accountRepository.save(cuenta1);
            accountRepository.save(cuenta2);
            accountRepository.save(cuenta4);


            Client client3 = new Client("Santi", "aragon", "SantyA@mindhub.com");
            clientRepository.save(client3);
            Account cuenta3 = new Account("VIN003", LocalDateTime.now(), 10000, client3);
            accountRepository.save(cuenta3);

            Client client4 = new Client("Ivan", "gomez", "IvanG@mindhub.com");
            Client client5 = new Client("Juan", "perez", "JuanP@mindhub.com");
            Client client6 = new Client("Maria", "gonzalez", "MariaG@mindhub.com");

            Transaction transaction1 = new Transaction(TransactionType.CREDITO, 550, "Transfer received from Rune Bank ", LocalDateTime.now(), cuenta1);
            transactionRepository.save(transaction1);
            Transaction transaction2 = new Transaction(TransactionType.DEBITO, 1099.99, "Riot Games 2500 RP", LocalDateTime.now(), cuenta1);
            transactionRepository.save(transaction2);
            Transaction transaction4 = new Transaction(TransactionType.DEBITO, 3099.99, "Riot Games 5500 RP", LocalDateTime.now(), cuenta1);
            transactionRepository.save(transaction4);

            Transaction transaction3 = new Transaction(TransactionType.CREDITO, 9900, "Moonton salary", LocalDateTime.now(), cuenta2);
            transactionRepository.save(transaction3);
            Transaction transaction5 = new Transaction(TransactionType.DEBITO, 3999, "Steam store", LocalDateTime.now(), cuenta2);
            transactionRepository.save(transaction5);
            Transaction transaction6 = new Transaction(TransactionType.DEBITO, 1650, "Mc Donalds", LocalDateTime.now(), cuenta2);
            transactionRepository.save(transaction6);
            Transaction transaction7 = new Transaction(TransactionType.DEBITO, 5300, "Supermarket", LocalDateTime.now(), cuenta2);
            transactionRepository.save(transaction7);

            clientRepository.save(client4);
            clientRepository.save(client5);
            clientRepository.save(client6);

            List<Integer> miLista = List.of(12, 24, 36, 48, 60);
            Loan loan1 = new Loan(Hipotecario, 500000, miLista);
            loanRepository.save(loan1);

            miLista = List.of(6, 12, 24);
            Loan loan2 = new Loan(Personal, 100000, miLista);
            loanRepository.save(loan2);

            miLista = List.of(6, 12, 24, 36);
            Loan loan3 = new Loan(Automotriz, 300000, miLista);
            loanRepository.save(loan3);


            ClientLoan clientloan1 = new ClientLoan(400000, 60, client1, loan1);
            ClientLoan clientloan2 = new ClientLoan(50000, 12, client1, loan2);
            clientLoanRepository.save(clientloan1);
            clientLoanRepository.save(clientloan2);


            ClientLoan clientloan3 = new ClientLoan(100000, 24, client3, loan2);
            ClientLoan clientloan4 = new ClientLoan(200000, 36, client3, loan3);
            clientLoanRepository.save(clientloan3);
            clientLoanRepository.save(clientloan4);

            Card card1 = new Card(client1.getFullName(), DEBIT, GOLD, "1234567890123456", 219, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);
            cardRepository.save(card1);

            Card card2 = new Card(client1.getFullName(), CREDIT, TITANIUM, "4000001234567899", 513, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);
            cardRepository.save(card2);

            Card card3 = new Card(client3.getFullName(), CREDIT, SILVER, "4000123456789010", 987, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client3);
            cardRepository.save(card3);

            Card card4 = new Card(client1.getFullName(), CREDIT, SILVER, "4000123456754321", 754, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);
            cardRepository.save(card4);

            System.out.println("todo listo, arranca nomas rey");
        };

    }

}
