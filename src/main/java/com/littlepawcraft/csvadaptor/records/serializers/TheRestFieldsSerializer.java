package com.littlepawcraft.csvadaptor.records.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.collections4.MultiValuedMap;

import java.io.IOException;
import java.util.Map;

public class TheRestFieldsSerializer extends JsonSerializer<MultiValuedMap<String, String>> {

    @Override
    public void serialize(MultiValuedMap<String, String> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        for (Map.Entry<String, String> entry : map.entries()) {
            jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }
}
