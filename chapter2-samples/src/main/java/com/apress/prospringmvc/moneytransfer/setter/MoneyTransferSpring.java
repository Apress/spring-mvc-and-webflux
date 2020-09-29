package com.apress.prospringmvc.moneytransfer.setter;

import com.apress.prospringmvc.ApplicationContextLogger;
import com.apress.prospringmvc.moneytransfer.domain.Transaction;
import com.apress.prospringmvc.moneytransfer.service.MoneyTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

/**
 * @author Marten Deinum
 */
public class MoneyTransferSpring {

	private static final Logger logger = LoggerFactory.getLogger(MoneyTransferSpring.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		var ctx1 = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);
		transfer(ctx1);

		ApplicationContextLogger.log(ctx1);
	}

	private static void transfer(ApplicationContext ctx) {
		var service = ctx.getBean("moneyTransferService", MoneyTransferService.class);
		var transaction = service.transfer("123456", "654321", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);
	}

}
