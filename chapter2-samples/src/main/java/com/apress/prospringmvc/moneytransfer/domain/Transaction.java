package com.apress.prospringmvc.moneytransfer.domain;

import org.springframework.core.style.ToStringCreator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Base class for transaction subclasses.
 *
 * @author Marten Deinum
 */
public abstract class Transaction {

	private final Account source;
	private final BigDecimal amount;
	private final Date date = new Date();

	protected Transaction(final Account source, final BigDecimal amount) {
		super();
		this.source = source;
		this.amount = amount;
	}

	public Account getSource() {
		return this.source;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public Date getDate() {
		return this.date;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("source", this.source)
				.append("amount", this.amount)
				.append("date", this.date).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, amount, date);
	}
}
