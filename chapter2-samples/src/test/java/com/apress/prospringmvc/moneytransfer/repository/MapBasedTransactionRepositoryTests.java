package com.apress.prospringmvc.moneytransfer.repository;

import com.apress.prospringmvc.moneytransfer.domain.Account;
import com.apress.prospringmvc.moneytransfer.domain.MoneyTransferTransaction;
import com.apress.prospringmvc.moneytransfer.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class MapBasedTransactionRepositoryTests {

    private final MapBasedTransactionRepository repository = new MapBasedTransactionRepository();

    @Test
    public void store() {
        Account src = new Account("src");
        Account dst = new Account("dst");
        BigDecimal amount = BigDecimal.TEN;
        MoneyTransferTransaction transaction = new MoneyTransferTransaction(src, dst, amount);

        this.repository.store(transaction);

        Iterable<Transaction> srcTxs = this.repository.find(src);
        Iterable<Transaction> dstTxs = this.repository.find(dst);

        assertThat(srcTxs)
                .hasSize(1)
                .containsOnly(transaction);

        assertThat(dstTxs)
                .hasSize(1)
                .containsOnly(transaction);

    }

    @Test
    public void findNonExisting() {
        Iterable<Transaction> transactions = this.repository.find(new Account("foobar"));
        assertThat(transactions).isEmpty();
    }

}
