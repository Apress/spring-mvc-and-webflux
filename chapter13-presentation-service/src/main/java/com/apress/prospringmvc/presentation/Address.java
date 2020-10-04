package com.apress.prospringmvc.presentation;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * A component which resembles the address of a {@link Account}
 * 
 * @author Marten Deinum

 * 
 */
//@Embeddable
@SuppressWarnings("serial")
public class Address implements Serializable {

    @NotEmpty
    private String street;
    private String houseNumber;
    private String boxNumber;

    @NotEmpty
    private String city;
    private String postalCode;

    @NotEmpty
    private String country;

    public Address() {
        super();
    }

    public Address(Address source) {
        super();
        this.street = source.street;
        this.houseNumber = source.houseNumber;
        this.boxNumber = source.boxNumber;
        this.city = source.city;
        this.postalCode = source.postalCode;
        this.country = source.country;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return this.houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getBoxNumber() {
        return this.boxNumber;

    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isEmpty(){
    	return street == null && houseNumber == null && boxNumber == null && city == null && postalCode == null && country == null;
		}

	@Override
	public String toString() {
		return "Address{" +
				"street='" + street + '\'' +
				", houseNumber='" + houseNumber + '\'' +
				", boxNumber='" + boxNumber + '\'' +
				", city='" + city + '\'' +
				", postalCode='" + postalCode + '\'' +
				", country='" + country + '\'' +
				'}';
	}
}