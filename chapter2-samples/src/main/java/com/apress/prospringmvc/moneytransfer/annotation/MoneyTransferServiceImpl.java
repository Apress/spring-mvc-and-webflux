package com.apress.prospringmvc.moneytransfer.annotation;

import com.apress.prospringmvc.moneytransfer.repository.AccountRepository;
import com.apress.prospringmvc.moneytransfer.repository.TransactionRepository;
import com.apress.prospringmvc.moneytransfer.service.AbstractMoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marten Deinum
 */
public class MoneyTransferServiceImpl extends AbstractMoneyTransferService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	protected AccountRepository getAccountRepository() {
		return this.accountRepository;
	}

	@Override
	protected TransactionRepository getTransactionRepository() {
		return this.transactionRepository;
	}
}
