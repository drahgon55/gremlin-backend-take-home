package com.gremlinja;

import com.gremlinja.dto.Quote;
import com.gremlinja.model.Args;
import com.gremlinja.service.Forismatic;

public class Main {
    public static void main(String[] args) {
        Args arguments = new Args(args);

        Quote quote = null;
        try {
            Forismatic forismaticService = new Forismatic();
            quote = forismaticService.getQuote(arguments.getLanguage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(4);
        }

        System.out.println(quote);
    }
}