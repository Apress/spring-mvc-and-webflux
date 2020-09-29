package com.apress.prospringmvc.moneytransfer.annotation.profiles;

import com.apress.prospringmvc.moneytransfer.domain.Account;
import com.apress.prospringmvc.moneytransfer.domain.Transaction;
import com.apress.prospringmvc.moneytransfer.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

/**
 * @author Marten Deinum
 */
public class StubTransactionRepository implements TransactionRepository {

	private final Logger logger = LoggerFactory.getLogger(StubTransactionRepository.class);

	@Override
	public Transaction store(Transaction transaction) {
		this.logger.info("Stored: {}", transaction);
		return transaction;
	}

	@Override
	public Iterable<Transaction> find(Account account) {
		return new HashSet<>();
	}

}
