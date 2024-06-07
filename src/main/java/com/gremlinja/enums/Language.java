package com.gremlinja.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Language {
    ENGLISH("English", "en"),
    RUSSIAN("Russian", "ru");

    @Getter
    private final String name;
    @Getter
    private final String code;

    public static String getCodeByName(String name) {
        for (Language lang : Language.values()) {
            if (lang.getName().equalsIgnoreCase(name)) {
                return lang.getCode();
            }
        }
        return null;
    }

    public static Language fromName(String name) {
        for (Language lang : Language.values()) {
            if (lang.getName().equalsIgnoreCase(name)) {
                return lang;
            }
        }
        return null;
    }
}
