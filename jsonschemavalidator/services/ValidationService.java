package com.embibe.jsonschemavalidator.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Set;
public class ValidationService {
    private final SchemaService schemaService;

    public ValidationService(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @SneakyThrows
    public Set<ValidationMessage> validate(String schemaId, JsonNode content) {
        JsonSchema jsonSchema = schemaService.getSchema(schemaId);
        return jsonSchema.validate(content);
    }
}
