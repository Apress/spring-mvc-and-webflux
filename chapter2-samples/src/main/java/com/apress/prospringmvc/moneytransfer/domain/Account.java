package com.apress.prospringmvc.moneytransfer.domain;

import org.springframework.core.style.ToStringCreator;

import java.math.BigDecimal;

/**
 * @author Marten Deinum
 */
public class Account {

	private final String number;
	private String owner;
	private BigDecimal balance = BigDecimal.ZERO;

	public Account(final String number) {
		super();
		this.number = number;
	}

	public void debit(BigDecimal amount) {
		this.balance = this.balance.add(amount);
	}

	public void credit(BigDecimal amount) {
		if (this.balance.compareTo(amount) < 0) {
			throw new IllegalArgumentException("Insufficient Funds!");
		}
		this.balance = this.balance.subtract(amount);
	}

	public String getNumber() {
		return this.number;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		return this.number.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("owner", this.owner)
				.append("number", this.number)
				.append("balance", this.balance).toString();
	}

}
