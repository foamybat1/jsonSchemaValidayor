package com.embibe.jsonschemavalidator.services;

import com.embibe.jsonschemavalidator.exceptions.InvalidJsonSchemaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersionDetector;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class SchemaService {
    private static final String INVALID_JSON_SCHEMA_DEFINED_0 = "Invalid Json Schema defined: {0}";
    private final Map<String, JsonNode> schemaMap;
    private final ObjectMapper objectMapper;

    protected SchemaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.schemaMap = new HashMap<>();
    }

    public JsonSchema getSchema(String id) {
        JsonNode jsonNode = this.schemaMap.computeIfAbsent(id, s -> {
            try {
                String schemaString = SchemaService.this.loads(id);
                return objectMapper.readTree(schemaString);
            } catch (JsonProcessingException e) {
                log.error(MessageFormat.format(INVALID_JSON_SCHEMA_DEFINED_0, id));
                throw new InvalidJsonSchemaException(e);
            }
        });
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(jsonNode));
        return jsonSchemaFactory.getSchema(jsonNode);
    }

    public abstract String loads(String id);
}
