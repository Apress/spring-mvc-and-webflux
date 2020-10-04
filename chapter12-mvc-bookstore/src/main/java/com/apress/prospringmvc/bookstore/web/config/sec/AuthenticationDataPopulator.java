/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.bookstore.web.config.sec;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.Address;
import com.apress.prospringmvc.bookstore.domain.Role;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This class is needed to create accounts for the users in the AuthenticationManager in-memory database.
 * @author Iuliana Cosmina
 * @date 07/06/2020
 */
@Service
public class AuthenticationDataPopulator {
	private Logger logger = LoggerFactory.getLogger(AuthenticationDataPopulator.class);

	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public AuthenticationDataPopulator(AccountRepository accountRepository, RoleRepository roleRepository) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
	}

	@PostConstruct
	private void init(){
		logger.info(" -->> Starting authentication data initialization...");
		roleRepository.saveAll(List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));

		Address address = new Address();
		address.setStreet("Liberty Street");
		address.setCity("of angels");
		address.setCountry("Europe");

		Account john = new Account();
		john.setFirstName("john");
		john.setUsername("john");
		john.setLastName("doe");
		john.setEmailAddress("john@doe.com");
		john.setPassword(passwordEncoder.encode("doe"));
		john.setAddress(address);
		john.setRoles(List.of(roleRepository.findByRole("ROLE_USER")));
		accountRepository.save(john);

		Account jane = new Account();
		jane.setFirstName("jane");
		jane.setLastName("doe");
		jane.setUsername("jane");
		jane.setEmailAddress("jane@doe.com");
		jane.setPassword(passwordEncoder.encode("doe"));
		jane.setAddress(address);
		jane.setRoles(List.of(roleRepository.findByRole("ROLE_USER"), roleRepository.findByRole("ROLE_ADMIN")));
		accountRepository.save(jane);

		Account admin = new Account();
		admin.setFirstName("admin");
		admin.setLastName("admin");
		admin.setUsername("admin");
		admin.setEmailAddress("admin@admin.com");
		admin.setPassword(passwordEncoder.encode("admin"));
		admin.setAddress(address);
		admin.setRoles(List.of(roleRepository.findByRole("ROLE_ADMIN")));
		accountRepository.save(admin);
		logger.info(" -->> Authentication data initialization completed.");
	}
}
