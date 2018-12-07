package org.acme.validation;

import javax.validation.constraints.*;

public class Book {
    

    @NotBlank(message="Title cannot be blank")    
    private String title;

    @NotBlank(message="Author cannot be blank")
    private String author;

    @Min(message="Author has been very lazy", value=1)
    private double pages;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPages() {
        return pages;
    }

    public void setPages(double pages) {
        this.pages = pages;
    }

}