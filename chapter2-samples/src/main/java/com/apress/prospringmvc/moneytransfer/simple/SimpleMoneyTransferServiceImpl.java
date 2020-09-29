package com.apress.prospringmvc.moneytransfer.simple;

import com.apress.prospringmvc.moneytransfer.repository.AccountRepository;
import com.apress.prospringmvc.moneytransfer.repository.MapBasedAccountRepository;
import com.apress.prospringmvc.moneytransfer.repository.MapBasedTransactionRepository;
import com.apress.prospringmvc.moneytransfer.repository.TransactionRepository;
import com.apress.prospringmvc.moneytransfer.service.AbstractMoneyTransferService;

/**
 * {@code MoneyTransferService} implementation which instantiates the needed beans itself.
 *
 * @author Marten Deinum
 */
public class SimpleMoneyTransferServiceImpl extends AbstractMoneyTransferService {

	private final AccountRepository accountRepository = new MapBasedAccountRepository();
	private final TransactionRepository transactionRepository = new MapBasedTransactionRepository();

	public SimpleMoneyTransferServiceImpl() {
		super();
		((MapBasedAccountRepository) this.accountRepository).initialize();

	}

	@Override
	protected AccountRepository getAccountRepository() {
		return this.accountRepository;
	}

	@Override
	protected TransactionRepository getTransactionRepository() {
		return this.transactionRepository;
	}

}
