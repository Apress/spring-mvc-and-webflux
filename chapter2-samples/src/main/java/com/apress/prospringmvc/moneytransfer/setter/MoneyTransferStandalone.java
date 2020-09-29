package com.apress.prospringmvc.moneytransfer.setter;

import com.apress.prospringmvc.moneytransfer.domain.Transaction;
import com.apress.prospringmvc.moneytransfer.repository.MapBasedAccountRepository;
import com.apress.prospringmvc.moneytransfer.repository.MapBasedTransactionRepository;
import com.apress.prospringmvc.moneytransfer.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author Marten Deinum
 */
public class MoneyTransferStandalone {

	private static final Logger logger = LoggerFactory.getLogger(MoneyTransferStandalone.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		var accountRepository = new MapBasedAccountRepository();
		accountRepository.initialize();
		var transactionRepository = new MapBasedTransactionRepository();
		var service = new MoneyTransferServiceImpl();
		service.setAccountRepository(accountRepository);
		service.setTransactionRepository(transactionRepository);

		var transaction = service.transfer("123456", "654321", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);

	}

}
