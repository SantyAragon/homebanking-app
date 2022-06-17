package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mindhub.HomeBanking.models.CardType.*;
import static com.mindhub.HomeBanking.models.ColorCard.*;
import static com.mindhub.HomeBanking.models.LoanType.*;

@SpringBootApplication
public class HomeBankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {

        SpringApplication.run(HomeBankingApplication.class, args);

    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
        return (args) -> {


            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
            Client client2 = new Client("Santi", "Aragon", "SantyA@mindhub.com", passwordEncoder.encode("santi123"));
            Client client3 = new Client("Ema", "Leiva", "EmaLeiva@admin.com", passwordEncoder.encode("ema123"));
            clientRepository.save(client1);
            clientRepository.save(client2);
            clientRepository.save(client3);

            Account cuenta1 = new Account(AccountType.SAVINGS, "VIN001", LocalDateTime.now(), 5000, client1);
            Account cuenta2 = new Account(AccountType.SAVINGS, "VIN002", LocalDateTime.now().plusDays(1), 7500, client1);
            Account cuenta3 = new Account(AccountType.SAVINGS, "VIN003", LocalDateTime.now(), 10000, client2);
//            Account cuenta4 = new Account(AccountType.SAVINGS,"VIN004", LocalDateTime.now(), 6500, client1);
            Account cuenta5 = new Account(AccountType.SAVINGS, "VIN005", LocalDateTime.now(), 25500, client3);
            Account cuenta6 = new Account(AccountType.SAVINGS, "VIN006", LocalDateTime.now(), 0, client3);
            accountRepository.save(cuenta1);
            accountRepository.save(cuenta2);
            accountRepository.save(cuenta3);
//            accountRepository.save(cuenta4);
            accountRepository.save(cuenta5);
            accountRepository.save(cuenta6);

            Transaction transaction1 = new Transaction(TransactionType.CREDIT, 550, "Transfer received from Rune Bank ", LocalDateTime.now(), cuenta1);
            Transaction transaction2 = new Transaction(TransactionType.DEBIT, 1099.99, "Riot Games 2500 RP", LocalDateTime.now(), cuenta1);
            Transaction transaction3 = new Transaction(TransactionType.CREDIT, 9900, "Moonton salary", LocalDateTime.now(), cuenta2);
            Transaction transaction4 = new Transaction(TransactionType.DEBIT, 3099.99, "Riot Games 5500 RP", LocalDateTime.now(), cuenta1);
            Transaction transaction5 = new Transaction(TransactionType.DEBIT, 3999, "Steam store", LocalDateTime.now(), cuenta2);
            Transaction transaction6 = new Transaction(TransactionType.DEBIT, 1650, "Mc Donalds", LocalDateTime.now(), cuenta2);
            Transaction transaction7 = new Transaction(TransactionType.DEBIT, 5300, "Supermarket", LocalDateTime.now(), cuenta2);
            Transaction transaction8 = new Transaction(TransactionType.DEBIT, 7300, "Mc Donalds", LocalDateTime.now(), cuenta5);
            Transaction transaction9 = new Transaction(TransactionType.CREDIT, 9300, "Supermarket", LocalDateTime.now(), cuenta5);

            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);
            transactionRepository.save(transaction5);
            transactionRepository.save(transaction6);
            transactionRepository.save(transaction7);
            transactionRepository.save(transaction8);
            transactionRepository.save(transaction9);


            List<Integer> miLista = List.of(12, 24, 36, 48, 60);
            Loan loan1 = new Loan("Mortgage", 500000, miLista, 25);

            miLista = List.of(6, 12, 24);
            Loan loan2 = new Loan("Personal", 100000, miLista, 12);

            miLista = List.of(6, 12, 24, 36);
            Loan loan3 = new Loan("Car", 300000, miLista, 35);

            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);


            ClientLoan clientloan1 = new ClientLoan(400000, 60, client1, loan1);
            ClientLoan clientloan2 = new ClientLoan(50000, 12, client1, loan2);
            clientLoanRepository.save(clientloan1);
            clientLoanRepository.save(clientloan2);

            ClientLoan clientloan3 = new ClientLoan(100000, 24, client3, loan2);
            ClientLoan clientloan4 = new ClientLoan(200000, 36, client3, loan3);
            clientLoanRepository.save(clientloan3);
            clientLoanRepository.save(clientloan4);

            Card card1 = new Card(client1.getFullName(), DEBIT, GOLD, "1234567890123456", 219, LocalDateTime.now().minusYears(7).minusMonths(3), LocalDateTime.now().minusYears(5).minusMonths(3), client1);
            Card card2 = new Card(client1.getFullName(), CREDIT, TITANIUM, "4000001234567899", 513, LocalDateTime.now().minusYears(2).minusMonths(1), LocalDateTime.now().minusYears(1).minusMonths(1), client1);
            Card card5 = new Card(client1.getFullName(), CREDIT, TITANIUM, "101341434567899", 513, LocalDateTime.now().minusYears(2).minusMonths(1), LocalDateTime.now().minusYears(1).minusMonths(3), client1);
            Card card3 = new Card(client3.getFullName(), CREDIT, SILVER, "4000123456789010", 987, LocalDateTime.now().minusYears(3).minusMonths(2), LocalDateTime.now().minusYears(4).minusMonths(5), client3);
            Card card4 = new Card(client1.getFullName(), CREDIT, SILVER, "4000123456754321", 754, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);

            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);
            cardRepository.save(card4);
            cardRepository.save(card5);

            System.out.println("todo listo, arranca nomas rey");


        };

    }

}
