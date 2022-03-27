package com.littlepawcraft.csvadaptor.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.littlepawcraft.csvadaptor.records.Record;

import java.io.*;

public class JsonOutputGenerator implements OutputGenerator {

    private final String outputDirectory;
    private final ObjectMapper mapper;

    public JsonOutputGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void apply(Record record) {
        File outFile = new File(this.outputDirectory + "/" + record.getLineNumber() + ".out.json");
        new File(outFile.getParent()).mkdirs();
        try (OutputStream out = new FileOutputStream(outFile)) {
            this.mapper.writeValue(out, record);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
