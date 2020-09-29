package com.apress.prospringmvc.moneytransfer.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class AccountTests {

    private static final BigDecimal THOUSAND = new BigDecimal("1000.00");
    private static final BigDecimal HUNDRED = new BigDecimal("100.00");

    @Test
    public void creation() {
        Account account = new Account("123456");

        assertThat(account)
                .hasFieldOrPropertyWithValue("number", "123456")
                .hasFieldOrPropertyWithValue("balance", BigDecimal.ZERO)
                .hasFieldOrPropertyWithValue("owner", null);

        account.setOwner("test");

        assertThat(account)
                .hasFieldOrPropertyWithValue("number", "123456")
                .hasFieldOrPropertyWithValue("balance", BigDecimal.ZERO)
                .hasFieldOrPropertyWithValue("owner", "test");

    }

    @Test
    public void debit() {
        Account account = new Account("123456");
        account.debit(THOUSAND);

        assertThat(account.getBalance()).isEqualByComparingTo(THOUSAND);

        account.debit(HUNDRED);

        assertThat(account.getBalance()).isEqualByComparingTo(THOUSAND.add(HUNDRED));
    }

    @Test
    public void creditInsufficientFunds() {

        Account account = new Account("123456");

        assertThatThrownBy(() -> account.credit(THOUSAND))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Insufficient Funds!");

    }

    @Test
    public void credit() {
        Account account = new Account("123456");
        account.debit(THOUSAND);

        assertThat(account.getBalance()).isEqualByComparingTo(THOUSAND);

        account.credit(HUNDRED);
        assertThat(account.getBalance()).isEqualByComparingTo(THOUSAND.subtract(HUNDRED));

    }

}
