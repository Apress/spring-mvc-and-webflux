import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;

import java.util.Date;

public class ConversionServiceTest {

	@Test
	public void testSpringBoot() {
		DateTimeFormatters dtfs = new DateTimeFormatters();
		dtfs.dateFormat("iso");
		WebConversionService conversionService = new WebConversionService(dtfs);
		User target = new User();
		DataBinder binder = new WebDataBinder(target, "user");
		binder.setConversionService(conversionService);
		binder.setRequiredFields("username", "dob");
		MutablePropertyValues values = new MutablePropertyValues();
		values.addPropertyValue("username", "test");
		values.addPropertyValue("dob", "06-05-1976");
		binder.bind(values);
		binder.validate();

		System.out.println(target);
	}

	@Test
	public void testPlainSpring() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		registrar.setFormatter(new DateFormatter("yyyy-MM-dd"));
		registrar.registerFormatters(conversionService);

		User target = new User();
		DataBinder binder = new WebDataBinder(target, "user");
		binder.setConversionService(conversionService);
		binder.setRequiredFields("username", "dob");
		MutablePropertyValues values = new MutablePropertyValues();
		values.addPropertyValue("username", "test");
		values.addPropertyValue("dob", "06-05-1976");
		binder.bind(values);
		binder.validate();

		System.out.println(target);
	}


	static class User {

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date dob;
		private String username;

		public void setUsername(String username) {
			this.username = username;
		}

		public String getUsername() {
			return username;
		}

		public Date getDob() {
			return dob;
		}
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		public void setDob(Date dob) {
			this.dob = dob;
		}

		@Override
		public String toString() {
			return "User{" +
					"dateOfBirth=" + dob +
					", username='" + username + '\'' +
					'}';
		}
	}

}
