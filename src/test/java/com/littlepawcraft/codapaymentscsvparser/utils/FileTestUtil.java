package com.littlepawcraft.codapaymentscsvparser.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTestUtil {
    public static String toAbsolutePath(String resourcePath) {
        File file = new File("target/test-classes");
        String absolutePath = file.getAbsolutePath();

        if (resourcePath.startsWith("/")) {
            return absolutePath + resourcePath;
        } else {
            return absolutePath + "/" + resourcePath;
        }

    }

    public static String loadFileAsString(String absolutePath) {
        try {
            return Files.readString(Path.of(absolutePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void checkJson(String jsonValue, String expectedJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode outputContentNormalized = mapper.readValue(jsonValue, JsonNode.class);
            JsonNode expectedValue = mapper.readValue(expectedJson, JsonNode.class);
            assertThat(outputContentNormalized).isEqualTo(expectedValue);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void checkXml(String xmlValue, String expectedXml) {
        try {
            XmlMapper mapper = new XmlMapper();
            JsonNode outputContentNormalized = mapper.readValue(xmlValue, JsonNode.class);
            JsonNode expectedValue = mapper.readValue(expectedXml, JsonNode.class);
            assertThat(outputContentNormalized).isEqualTo(expectedValue);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
