package de.fiserv.ipg;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.fiserv.ipg.entity.Transaction;
import de.fiserv.ipg.repository.TransactionRepository;

@SpringBootApplication
public class PaymentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentAppApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(TransactionRepository repository) {
        return args -> {
            List<Transaction> transactions = new ArrayList<>();
            Random rand = new Random();
            for (int i = 1; i <= 20; i++) {
                Transaction.TransactionType transactionType = (i % 2 == 0) ? Transaction.TransactionType.SALE : Transaction.TransactionType.REFUND;
                Transaction.Status status = Transaction.Status.values()[rand.nextInt(Transaction.Status.values().length)];
                Transaction.Currency currency = Transaction.Currency.values()[rand.nextInt(Transaction.Currency.values().length)];

                transactions.add(new Transaction(
                        new BigDecimal(rand.nextInt(1000)), // Random amount between 0-999
                        LocalDateTime.now().minusDays(rand.nextInt(30)), // Random timestamp within the last 30 days
                        transactionType,
                        status,
                        currency));
            }
            repository.saveAll(transactions);
        };
    }
}
