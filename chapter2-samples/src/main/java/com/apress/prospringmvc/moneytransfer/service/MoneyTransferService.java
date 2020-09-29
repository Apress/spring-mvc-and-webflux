package com.apress.prospringmvc.moneytransfer.service;

import com.apress.prospringmvc.moneytransfer.domain.Transaction;

import java.math.BigDecimal;

/**
 * Service contract for transfering money from one account to another.
 *
 * @author Marten Deinum
 */
public interface MoneyTransferService {

	/**
	 * Transfer a <code>amount</code> of money from the <code>source</code> to <code>target</code> account
	 *
	 * @param source source account number
	 * @param target target account number
	 * @param amount the amount to transfer
	 * @return the {@link Transaction} created
	 */
	Transaction transfer(String source, String target, BigDecimal amount);

}
