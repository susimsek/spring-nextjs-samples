package io.github.susimsek.springnextjssamples.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstraintMessage {

    public static final String NOT_NULL = "{validation.field.notNull}";
    public static final String NOT_BLANK = "{validation.field.notBlank}";
    public static final String SIZE = "{validation.field.size}";
    public static final String PATTERN = "{validation.field.pattern}";
    public static final String MIN = "{validation.field.min}";
    public static final String MAX = "{validation.field.max}";
    public static final String EMAIL = "{validation.field.email}";
    public static final String PAST = "{validation.field.past}";
    public static final String FUTURE = "{validation.field.future}";
    public static final String URL = "{validation.field.url}";
    public static final String ENUM = "{validation.field.enum}";
    public static final String IP_ADDRESS = "{validation.field.ipAddress}";
    public static final String PHONE_NUMBER = "{validation.field.phoneNumber}";
    public static final String TCKN = "{validation.field.tckn}";
    public static final String STRONG_PASSWORD = "{validation.field.strongPassword}";
    public static final String NO_SPECIAL_CHARACTERS = "{validation.field.noSpecialCharacters}";
    public static final String ALPHANUMERIC = "{validation.field.alphanumeric}";
    public static final String RANGE = "{validation.field.range}";
    public static final String DATE_RANGE = "{validation.field.dateRange}";
}
