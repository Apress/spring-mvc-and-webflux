package com.apress.prospringmvc.moneytransfer.service;

import com.apress.prospringmvc.moneytransfer.domain.Account;
import com.apress.prospringmvc.moneytransfer.domain.MoneyTransferTransaction;
import com.apress.prospringmvc.moneytransfer.domain.Transaction;
import com.apress.prospringmvc.moneytransfer.repository.AccountRepository;
import com.apress.prospringmvc.moneytransfer.repository.TransactionRepository;

import java.math.BigDecimal;

/**
 * Base class for {@link MoneyTransferService} implementations. This class implements the business logic to transfer
 * money. Subclasses must implement the techniques to retrieve the repositories.
 *
 * @author Marten Deinum
 */
public abstract class AbstractMoneyTransferService implements MoneyTransferService {

	@Override
	public Transaction transfer(String source, String target, BigDecimal amount) {
		var src = getAccountRepository().find(source);
		var dst = getAccountRepository().find(target);

		src.credit(amount);
		dst.debit(amount);

		var transaction = new MoneyTransferTransaction(src, dst, amount);
		return getTransactionRepository().store(transaction);
	}

	protected abstract AccountRepository getAccountRepository();

	protected abstract TransactionRepository getTransactionRepository();

}
