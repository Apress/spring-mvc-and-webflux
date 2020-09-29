package com.apress.prospringmvc.moneytransfer.jndi;

import com.apress.prospringmvc.moneytransfer.repository.AccountRepository;
import com.apress.prospringmvc.moneytransfer.repository.TransactionRepository;
import com.apress.prospringmvc.moneytransfer.service.AbstractMoneyTransferService;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Marten Deinum
 */
public class JndiMoneyTransferServiceImpl extends AbstractMoneyTransferService {

	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;

	public JndiMoneyTransferServiceImpl() {
		try {
			var context = new InitialContext();
			this.accountRepository = (AccountRepository) context.lookup("accountRepository");
			this.transactionRepository = (TransactionRepository) context.lookup("transactionRepository");
		} catch (NamingException e) {
			throw new IllegalStateException(e);
		}
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
