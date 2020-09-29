package com.apress.prospringmvc.bookstore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@SuppressWarnings("serial")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BigDecimal price;
    private Integer year;
    private String author;
    @NaturalId
    private String isbn;

    @ManyToOne(optional = false)
    private Category category;

    public Long getId() {
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

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
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
