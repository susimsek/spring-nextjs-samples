package io.github.susimsek.springnextjssamples.config.graphql.scalars;

import graphql.GraphQLContext;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeScalar {

    public static final GraphQLScalarType DATE_TIME;

    static {
        Coercing<Instant, String> coercing = new Coercing<>() {

            @Override
            public String serialize(@NonNull Object dataFetcherResult, @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingSerializeException {
                if (dataFetcherResult instanceof Instant) {
                    return ((Instant) dataFetcherResult).toString();
                } else if (dataFetcherResult instanceof String) {
                    return parseInstant(dataFetcherResult.toString()).toString();
                } else {
                    throw new CoercingSerializeException("Expected type 'Instant' or 'String' but was '" + dataFetcherResult.getClass().getSimpleName() + "'.");
                }
            }

            @Override
            public Instant parseValue(@NonNull Object input, @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingParseValueException {
                if (input instanceof String) {
                    return parseInstant(input.toString());
                } else {
                    throw new CoercingParseValueException("Expected a 'String' but was '" + input.getClass().getSimpleName() + "'.");
                }
            }

            @Override
            public Instant parseLiteral(@NonNull Value<?> input, @NonNull graphql.execution.CoercedVariables variables, @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingParseLiteralException {
                if (input instanceof StringValue) {
                    return parseInstant(((StringValue) input).getValue());
                } else {
                    throw new CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + input.getClass().getSimpleName() + "'.");
                }
            }

            private Instant parseInstant(String input) {
                try {
                    return Instant.parse(input);
                } catch (DateTimeParseException e) {
                    throw new CoercingParseValueException("Invalid ISO-8601 DateTime format: '" + input + "'. Error: " + e.getMessage());
                }
            }
        };

        DATE_TIME = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("An ISO-8601 compliant DateTime scalar for Instant")
            .coercing(coercing)
            .build();
    }
}
