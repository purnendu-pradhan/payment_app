package de.fiserv.ipg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import de.fiserv.ipg.controller.TransactionController;
import de.fiserv.ipg.entity.Transaction;
import de.fiserv.ipg.entity.Transaction.Currency;
import de.fiserv.ipg.entity.Transaction.Status;
import de.fiserv.ipg.entity.Transaction.TransactionType;
import de.fiserv.ipg.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionByIdFound() {
        Transaction transaction = new Transaction(new BigDecimal("100"), LocalDateTime.now(), TransactionType.SALE, Status.APPROVED, Currency.EUR);
        transaction.setId(1L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transaction, response.getBody());
    }

    @Test
    void testGetTransactionByIdNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            transactionController.getTransactionById(1L);
        } catch (Exception e) {
            assertEquals("Transaction not found with ID: 1", e.getMessage());
        }
    }
}
