package com.apress.prospringmvc.moneytransfer.repository;

import com.apress.prospringmvc.moneytransfer.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MapBasedAccountRepositoryTests {

    private final MapBasedAccountRepository repository = new MapBasedAccountRepository();

    @BeforeEach
    public void setup() {
        //Need to simulate calling of @PostContruct annotated method.
        this.repository.initialize();
    }

    @Test
    public void nonExistingAccount() {
    	assertThatThrownBy(() -> this.repository.find("foobar"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageStartingWith("Non-Existing account ");
    }

    @Test
    public void existingAccount() {
        Account account = this.repository.find("123456");
        assertThat(account)
						.isNotNull()
						.hasFieldOrPropertyWithValue("number", "123456");
    }

    @Test
    public void storeAndFind() {
        Account account = new Account("foobar");
        account.setOwner("F. BarBaz");
        this.repository.store(account);

        assertThat(this.repository.find("foobar")).isEqualTo(account);
    }
}
