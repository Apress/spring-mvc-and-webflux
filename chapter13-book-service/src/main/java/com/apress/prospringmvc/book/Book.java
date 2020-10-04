package com.apress.prospringmvc.book;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

@Document(collection="book")
public class Book {
	public static class Category {
		public static final String SPRING = "Spring";
		public static final String JAVA = "Java";
		public static final String WEB = "Web";
	}

	  @Id
    private String id;

    private String title;
    private String description;
    private BigDecimal price;
    private Integer year;
    private String author;

	  @Indexed(unique = true)
    private String isbn;

	  // Just limit to {"Spring", "Java", "Web"}
    //just embed here
    private String category;

	public Book() {
	}

	public Book(String title, BigDecimal price, Integer year, String author, String isbn, String category) {
		this.title = title;
		this.price = price;
		this.year = year;
		this.author = author;
		this.isbn = isbn;
		this.category = category;
	}

	public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
        return Objects.equals(this.isbn, other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isbn);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("title", this.title);
        builder.append("author", this.author);
        builder.append("isbn", this.isbn);
        return builder.build();
    }
}
