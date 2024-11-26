package io.github.susimsek.springnextjssamples.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.Locale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.context.i18n.LocaleContextHolder;

class NumberFormatUtilsParameterizedTest {

    @ParameterizedTest
    @CsvSource({
        "12345.67, tr-TR, '12.345,67'",
        "12345.67, en-US, '12,345.67'",
        "12345.67, fr-FR, '12 345,67'"
    })
    void testFormatNumberWithProvidedLocale(BigDecimal amount, String localeString, String expected) {
        Locale locale = Locale.forLanguageTag(localeString);

        String formattedAmount = NumberFormatUtils.formatNumber(amount, locale);

        assertEquals(expected, formattedAmount,
            "Formatting for Locale " + localeString + " should match expected value.");
    }

    @ParameterizedTest
    @CsvSource({
        "12345.67, en-US, '12,345.67'",
        "12345.67, tr-TR, '12.345,67'"
    })
    void testFormatNumberUsingLocaleContextHolder(BigDecimal amount, String localeString, String expected) {
        Locale locale = Locale.forLanguageTag(localeString);
        LocaleContextHolder.setLocale(locale);

        String formattedAmount = NumberFormatUtils.formatNumber(amount);

        assertEquals(expected, formattedAmount,
            "Formatting with LocaleContextHolder for " + localeString + " should match expected value.");
    }

    @ParameterizedTest
    @NullSource
    void testFormatNumberWithNullAmount(BigDecimal amount) {

        String formattedAmount = NumberFormatUtils.formatNumber(amount);

        assertNull(formattedAmount, "Formatting null amount should return null.");
    }

    @ParameterizedTest
    @CsvSource({
        "12345.67, ''",
        "12345.67,",
        ",",
        ",tr-TR",
    })
    void testFormatNumberWithNullLocale(BigDecimal amount, String localeString) {
        Locale locale = localeString == null || localeString.isEmpty() ? null : Locale.forLanguageTag(localeString);

        String formattedAmount = NumberFormatUtils.formatNumber(amount, locale);

        assertNull(formattedAmount, "Formatting with null Locale should return null.");
    }
}
