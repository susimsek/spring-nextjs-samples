package io.github.susimsek.springnextjssamples.config.logging.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.susimsek.springnextjssamples.config.logging.LoggingProperties;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class DefaultObfuscationStrategy implements ObfuscationStrategy {

    private final LoggingProperties loggingProperties;
    private final ObjectMapper objectMapper;

    @Override
    public HttpHeaders maskHeaders(HttpHeaders headers) {
        HttpHeaders maskedHeaders = new HttpHeaders();
        headers.forEach((key, value) -> maskedHeaders.addAll(key,
            shouldMask(loggingProperties.getObfuscate().getHeaders(), key)
                ? List.of(loggingProperties.getObfuscate().getMaskValue())
                : value));
        return maskedHeaders;
    }

    @Override
    public String maskBody(String body) {
        if (!StringUtils.hasText(body)) {
            return body;
        }
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            maskJsonPaths(rootNode, loggingProperties.getObfuscate().getJsonBodyFields());
            return objectMapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            return body;
        }
    }

    @Override
    public URI maskUriParameters(URI uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);
        MultiValueMap<String, String> queryParams = builder.build().getQueryParams();
        MultiValueMap<String, String> maskedParams = new LinkedMultiValueMap<>();

        queryParams.forEach((key, value) -> maskedParams.addAll(key,
            shouldMask(loggingProperties.getObfuscate().getParameters(), key)
                ? List.of(loggingProperties.getObfuscate().getMaskValue())
                : value));

        builder.replaceQueryParams(maskedParams);
        return builder.build().toUri();
    }

    @Override
    public Object[] maskArguments(Object[] arguments) {
        if (arguments == null) {
            return null;
        }
        Object[] maskedArguments = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            maskedArguments[i] = maskObject(arguments[i]);
        }
        return maskedArguments;
    }

    @Override
    public Object maskResult(Object result) {
        return maskObject(result);
    }

    private void maskJsonPaths(JsonNode rootNode, List<String> jsonPaths) {
        for (String jsonPath : jsonPaths) {
            String[] pathParts = splitJsonPath(jsonPath.replace("$.", ""));
            maskJsonNodeRecursive(rootNode, pathParts, 0);
        }
    }

    private String[] splitJsonPath(String jsonPath) {
        List<String> pathParts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean escaped = false;

        for (char c : jsonPath.toCharArray()) {
            if (c == '\\' && !escaped) {
                escaped = true;
            } else if (c == '.' && !escaped) {
                pathParts.add(currentPart.toString());
                currentPart.setLength(0);
            } else {
                escaped = false;
                currentPart.append(c);
            }
        }

        pathParts.add(currentPart.toString());
        return pathParts.toArray(new String[0]);
    }

    private String unescapePart(String part) {
        return part.replace("\\.", ".");
    }

    private void maskJsonNodeRecursive(JsonNode currentNode, String[] pathParts, int index) {
        if (currentNode == null || index >= pathParts.length) {
            return;
        }

        String currentPart = unescapePart(pathParts[index]);

        if (isArraySegment(currentPart)) {
            processArraySegment(currentNode, pathParts, index, currentPart);
        } else if (currentPart.equals("*")) {
            if (index + 1 == pathParts.length) {
                maskAllFields(currentNode);
            } else {
                processWildcardSegment(currentNode, pathParts, index);
            }
        } else if (currentNode.has(currentPart)) {
            processObjectSegment(currentNode, pathParts, index, currentPart);
        }
    }

    private boolean isArraySegment(String currentPart) {
        return currentPart.contains("[*]");
    }

    private void processArraySegment(JsonNode currentNode, String[] pathParts, int index, String currentPart) {
        String arrayPart = currentPart.split("\\[\\*]")[0];
        if (currentNode.has(arrayPart) && currentNode.get(arrayPart).isArray()) {
            for (JsonNode arrayItem : currentNode.get(arrayPart)) {
                if (index + 1 == pathParts.length) {
                    maskAllFields(arrayItem);
                } else {
                    maskJsonNodeRecursive(arrayItem, pathParts, index + 1);
                }
            }
        }
    }

    private void processWildcardSegment(JsonNode currentNode, String[] pathParts, int index) {
        currentNode.fieldNames().forEachRemaining(fieldName ->
            maskJsonNodeRecursive(currentNode.get(fieldName), pathParts, index + 1)
        );
    }

    private void processObjectSegment(JsonNode currentNode, String[] pathParts, int index, String currentPart) {
        JsonNode nextNode = currentNode.get(currentPart);
        if (index + 1 == pathParts.length) {
            maskField((ObjectNode) currentNode, currentPart);
        } else {
            maskJsonNodeRecursive(nextNode, pathParts, index + 1);
        }
    }

    private void maskField(ObjectNode currentNode, String fieldName) {
        currentNode.set(
            fieldName,
            JsonNodeFactory.instance.textNode(
                loggingProperties.getObfuscate().getMaskValue()
            )
        );
    }

    private void maskAllFields(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.fieldNames().forEachRemaining(fieldName ->
                maskField(objectNode, fieldName)
            );
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayItem = arrayNode.get(i);
                if (arrayItem.isObject()) {
                    maskAllFields(arrayItem);
                } else {
                    arrayNode.set(i, JsonNodeFactory.instance.textNode(
                        loggingProperties.getObfuscate().getMaskValue()
                    ));
                }
            }
        }
    }

    private boolean shouldMask(List<String> list, String key) {
        return list.stream().anyMatch(item -> item.equalsIgnoreCase(key));
    }

    private Object maskObject(Object object) {
        if (object == null) {
            return null;
        }
        try {
            JsonNode rootNode = objectMapper.valueToTree(object);
            maskJsonPaths(rootNode, loggingProperties.getObfuscate().getMethodFields());
            return objectMapper.treeToValue(rootNode, Object.class);
        } catch (Exception e) {
            return object;
        }
    }
}
