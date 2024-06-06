package com.gremlinja.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gremlinja.dto.ForismaticResponse;
import com.gremlinja.dto.Quote;
import com.gremlinja.enums.Language;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForismaticTest {

    MockedStatic<HttpClients> httpClientsStaticMock;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);

        httpClientsStaticMock = mockStatic(HttpClients.class);
        httpClientsStaticMock.when(HttpClients::createDefault).thenReturn(httpClient);
    }

    @AfterEach
    public void close() {
        httpClientsStaticMock.close();
    }

    @Test
    void testGetQuote() throws Exception {
        ForismaticResponse forismaticResponse = new ForismaticResponse();
        forismaticResponse.setQuoteText("The only way to do great work is to love what you do.");
        forismaticResponse.setQuoteAuthor("Steve Jobs");

        String jsonResponse = objectMapper.writeValueAsString(forismaticResponse);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(new StringEntity(jsonResponse));

        Forismatic forismatic = new Forismatic();
        Quote quote = forismatic.getQuote(Language.ENGLISH);

        assertNotNull(quote);
        assertEquals(forismaticResponse.getQuoteText(), quote.getQuote());
        assertEquals(forismaticResponse.getQuoteAuthor(), quote.getAuthor());
    }

    @Test
    void testGetQuoteHttpError() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getCode()).thenReturn(404);
        when(httpResponse.getReasonPhrase()).thenReturn("Not Found");

        Forismatic forismatic = new Forismatic();
        assertThrows(HttpResponseException.class, () -> {
            forismatic.getQuote(Language.ENGLISH);
        });
    }

    @Test
    void testGetQuoteIOException() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(IOException.class);

        Forismatic forismatic = new Forismatic();
        assertThrows(IOException.class, () -> {
            forismatic.getQuote(Language.ENGLISH);
        });
    }
}
