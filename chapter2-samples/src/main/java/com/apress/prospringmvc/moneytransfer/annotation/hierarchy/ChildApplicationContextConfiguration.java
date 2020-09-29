package com.apress.prospringmvc.moneytransfer.annotation.hierarchy;

import com.apress.prospringmvc.moneytransfer.annotation.MoneyTransferServiceImpl;
import com.apress.prospringmvc.moneytransfer.service.MoneyTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marten Deinum
 */
@Configuration
public class ChildApplicationContextConfiguration {

	@Bean
	public MoneyTransferService moneyTransferService() {
		return new MoneyTransferServiceImpl();
	}

}
