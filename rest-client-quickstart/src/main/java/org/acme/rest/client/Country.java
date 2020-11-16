package org.acme.rest.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    public String name;
    public String alpha2Code;
    public String capital;
    public List<Currency> currencies;

    public static class Currency {
        public String code;
        public String name;
        public String symbol;
    }

}
