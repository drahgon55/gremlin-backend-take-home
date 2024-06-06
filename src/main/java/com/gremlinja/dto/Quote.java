package com.gremlinja.dto;

import lombok.Data;

@Data
public class Quote {
    private String quote;
    private String author;

    @Override
    public String toString() {
        return quote.trim() + " -" + author.trim();
    }
}
