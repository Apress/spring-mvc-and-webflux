package com.apress.prospringmvc.newreleases;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import java.math.BigDecimal;
import java.util.Objects;

public class Book {
    private String title;
    private Integer year;
    private String author;


	  // Just limit to {"Spring", "Java", "Web"}
    //just embed here
    private String category;

	public Book() {
	}

	public Book(String title, Integer year, String author) {
		this.title = title;
		this.year = year;
		this.author = author;
	}

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Book other = (Book) object;
        return Objects.equals(this.title, other.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("title", this.title);
        builder.append("author", this.author);
        return builder.build();
    }
}
