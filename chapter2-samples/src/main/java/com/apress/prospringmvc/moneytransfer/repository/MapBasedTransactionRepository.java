package com.apress.prospringmvc.moneytransfer.repository;

import com.apress.prospringmvc.moneytransfer.domain.Account;
import com.apress.prospringmvc.moneytransfer.domain.MoneyTransferTransaction;
import com.apress.prospringmvc.moneytransfer.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marten Deinum
 */
@Repository("transactionRepository")
public class MapBasedTransactionRepository implements TransactionRepository {

	private final Logger logger = LoggerFactory.getLogger(MapBasedTransactionRepository.class);

	private final Map<Account, Set<Transaction>> storage = new ConcurrentHashMap<>();

	@Override
	public Transaction store(Transaction transaction) {
		store(transaction.getSource(), transaction);
		if (transaction instanceof MoneyTransferTransaction) {
			var mtt = (MoneyTransferTransaction) transaction;
			store(mtt.getTarget(), transaction);
		}
		return transaction;
	}

	private void store(Account account, Transaction transaction) {
		var transactions = this.storage.computeIfAbsent(account, k -> new HashSet<>());
		transactions.add(transaction);
		this.logger.info("Stored transaction: {} for account {}.", transaction, account.getNumber());
	}

	@Override
	public Iterable<Transaction> find(Account account) {
		var transactions = this.storage.getOrDefault(account, Collections.emptySet());
		return Collections.unmodifiableSet(transactions);
	}
}
