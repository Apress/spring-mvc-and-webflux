package com.apress.prospringmvc.bookstore.document;

import com.apress.prospringmvc.bookstore.util.formatter.DateFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A account resembles an authenticated user of our system. A account is able to submit orders. A account is identified
 * by his or her username. When authenticating the user supplies its username and password. Besides identification
 * information we also store basic legal information such as address, firstname, lastname and email address.
 *
 * @author Marten Deinum

 *
 */
@Document(collection="account")
@SuppressWarnings("serial")
public class Account implements Serializable {

	public static class Authority {
		public static final String ROLE_USER = "ROLE_USER";
		public static final String ROLE_ADMIN = "ROLES_ADMIN";
	}

    @Id
    private String id;

    private String firstName;
    private String lastName;

    @DateFormat(format = "YYYY-MM-DD")
    private Date dateOfBirth;

    @Valid
		// mongo one-to-one, embedded
    private Address address = new Address();

    @NotEmpty
    @Email
    private String emailAddress;

    @NotEmpty
		@Indexed(unique = true)
    private String username;

    @NotEmpty
    private String password;

    private List<Order> orders;

    // make sure to restrict values to a limited set like ("ROLE_USER", "ROLE_ADMIN")
		// mongo one-to-few, embedded
    private List<String> roles = new ArrayList<>();

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Account addOrder(Order order){
    	this.orders.add(order);
    	order.setAccount(this);
    	return this;
	}

	@Override
	public String toString() {
		return "Account{" +
				"id='" + id + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", dateOfBirth=" + dateOfBirth +
				", address=" + address +
				", emailAddress='" + emailAddress + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", roles=" + roles + '\'' +
				", orders=" + orders +
				'}';
	}
}