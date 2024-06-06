package com.gremlinja.model;

import com.gremlinja.enums.Language;
import lombok.Data;

@Data
public class Args {
    private Language language;

    public Args(String[] args) {
        if (args.length > 1) {
            System.out.println("Too many arguments passed in, Only a single language should be passed in");
            System.exit(1);
        }

        if(args.length == 0) {
            System.out.println("Must pass in a language");
            System.exit(2);
        }

        Language language = Language.fromName(args[0].toUpperCase());

        if(language == null) {
            System.out.println("Passed in language must be either English or Russian");
            System.exit(3);
        }

        this.setLanguage(language);
    }
}


