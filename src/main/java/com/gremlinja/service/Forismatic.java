package com.gremlinja.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gremlinja.dto.ForismaticResponse;
import com.gremlinja.dto.Quote;
import com.gremlinja.enums.Language;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class Forismatic {
    private String url;

    public Forismatic() throws Exception {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream input = loader.getResourceAsStream("config.properties")) {
            properties.load(input);
        }

        this.url = properties.getProperty("api.url");

        if(this.url == null) {
            throw new Exception("Url not found in config.properties");
        }
    }

    public Quote getQuote(Language language) throws IOException, URISyntaxException, ParseException {
        Quote quote = null;
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(this.url)
                    .addParameter("method", "getQuote")
                    .addParameter("format", "json")
                    .addParameter("lang", language.getCode());

            HttpGet request = new HttpGet(uriBuilder.build());
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getCode() == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

                    ForismaticResponse forismaticResponse = objectMapper.readValue(jsonResponse, ForismaticResponse.class);

                    quote = new Quote();
                    quote.setQuote(forismaticResponse.getQuoteText());
                    quote.setAuthor(forismaticResponse.getQuoteAuthor());
                } else {
                    throw new HttpResponseException(response.getCode(), response.getReasonPhrase());
                }
            }
        }

        assert quote != null;
        return quote;
    }
}
