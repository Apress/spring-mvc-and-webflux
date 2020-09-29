package com.apress.prospringmvc.bookstore.domain;

import com.apress.prospringmvc.bookstore.formatter.DateFormat;

import javax.persistence.*;
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
@Entity
@SuppressWarnings("serial")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @DateFormat(format = "YYYY-MM-DD")
    private Date dateOfBirth;

    @Embedded
    @Valid
    private Address address = new Address();

    @NotEmpty
    @Email
    private String emailAddress;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    public Long getId() {
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

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}