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
package com.apress.prospringmvc.bookstore.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Iuliana Cosmina on 06/06/2020
 */
@Configuration
public class DbConfiguration {

	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put("hibernate.dialect", connectionProperties().getProperty("db.dialect"));
		hibernateProp.put("hibernate.hbm2ddl.auto", connectionProperties().getProperty("db.hbm2ddl"));

		hibernateProp.put("hibernate.format_sql", true);
		hibernateProp.put("hibernate.use_sql_comments", false); // set this on 'true' if you want to see the generated sql
		hibernateProp.put("hibernate.show_sql", false); // set this on 'true' if you want to see the generated sql in a readable manner
		return hibernateProp;
	}

	@Bean("connectionProperties")
	Properties connectionProperties(){
		try {
			return PropertiesLoaderUtils.loadProperties(
					new ClassPathResource("db.properties"));
		} catch (IOException e) {
			System.err.println("Well, though luck!");
			return null;
		}
	}

	@Bean
	public DataSource dataSource() {
		try {
			HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setDriverClassName(connectionProperties().getProperty("db.driverClassName"));
			hikariConfig.setJdbcUrl(connectionProperties().getProperty("db.url"));
			hikariConfig.setUsername(connectionProperties().getProperty("db.username"));
			hikariConfig.setPassword(connectionProperties().getProperty("db.password"));

			hikariConfig.setMaximumPoolSize(5);
			hikariConfig.setPoolName("booksPool");
			return new HikariDataSource(hikariConfig);
		} catch (Exception e) {
			return null;
		}
	}

	//needed because Hibernate does not drop the database and delete this file as it should
	@PostConstruct
	void discardDatabase(){
		final String currentDir = System.getProperty("user.dir");
		String dbUrl = connectionProperties().getProperty("db.url");
		int start = dbUrl.indexOf("./")+ 2;
		int end = dbUrl.indexOf(";", start);
		String dbName = dbUrl.substring(start, end);
		File one  = new File(currentDir.concat(File.separator).concat(dbName).concat(".mv.db"));
		one.deleteOnExit();
		File two  = new File(currentDir.concat(File.separator).concat(dbName).concat(".trace.db"));
		two.deleteOnExit();
	}
}
