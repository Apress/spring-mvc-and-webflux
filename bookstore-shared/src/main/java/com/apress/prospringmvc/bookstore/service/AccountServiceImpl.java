package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional(readOnly = false)
	public Account save(Account account) {
		Account acc = getAccount(account.getUsername());
		if(acc == null) {
			account.setPassword(sha256Hex(account.getPassword() + "{" + account.getUsername() + "}"));
		}
		return this.accountRepository.save(account);
	}

	@Override
	public Account login(String username, String password) throws AuthenticationException {
		var account = this.accountRepository.findByUsername(username);
		if (account != null) {
			var pwd = sha256Hex(password + "{" + username + "}");
			if (!account.getPassword().equalsIgnoreCase(pwd)) {
				throw new AuthenticationException("Wrong username/password combination.", "invalid.password");
			}
		} else {
			throw new AuthenticationException("Wrong username/password combination.", "invalid.username");
		}

		return account;
	}

	@Override
	public Account getAccount(String username) {
		return this.accountRepository.findByUsername(username);
	}

	private String sha256Hex(String text) {
		try {
			var digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Error creating hash.", e);
		}
	}
}
