package io.github.susimsek.springnextjssamples.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import lombok.experimental.UtilityClass;
import org.springframework.context.i18n.LocaleContextHolder;

@UtilityClass
public class NumberFormatUtils {

    /**
     * Format a BigDecimal as a localized number using a provided Locale.
     *
     * @param amount the BigDecimal to format
     * @param locale the Locale for formatting
     * @return the formatted string or null if amount or locale is null
     */
    public String formatNumber(BigDecimal amount, Locale locale) {
        if (amount == null || locale == null) {
            return null;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        return numberFormat.format(amount);
    }

    /**
     * Format a BigDecimal as a localized number using the Locale from LocaleContextHolder.
     *
     * @param amount the BigDecimal to format
     * @return the formatted string or null if amount is null
     */
    public String formatNumber(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        Locale locale = LocaleContextHolder.getLocale();
        return formatNumber(amount, locale);
    }
}
