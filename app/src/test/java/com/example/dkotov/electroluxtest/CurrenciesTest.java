package com.example.dkotov.electroluxtest;

import org.junit.Assert;
import org.junit.Test;

import model.Currencies;

import static org.junit.Assert.*;

/**
 * Created by dkotov on 25-Sep-17.
 */
public class CurrenciesTest {
    @Test
    public void getEnumByString() throws Exception {
        String currency1 = "US Dollar";
        String currency2 = "Swedish Krona";

        Assert.assertEquals("USD", Currencies.getEnumByString(currency1));
        assertEquals("SEK", Currencies.getEnumByString(currency2));
    }

}