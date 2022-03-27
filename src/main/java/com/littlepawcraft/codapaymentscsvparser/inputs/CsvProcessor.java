package com.littlepawcraft.codapaymentscsvparser.inputs;

import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileDuplicateHeadersException;
import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileEmptyHeadersException;
import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileNoHeaderException;
import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileNoRecordException;
import com.littlepawcraft.codapaymentscsvparser.outputs.ErrorReporter;
import com.littlepawcraft.codapaymentscsvparser.outputs.OutputGenerator;
import com.littlepawcraft.codapaymentscsvparser.records.Record;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CsvProcessor {

    private String csvFileLocation;
    private OutputGenerator outputGenerator;
    private ErrorReporter errorReporter;
    private Class<? extends Record> recordType;

    public CsvProcessor(String csvFileLocation, OutputGenerator outputGenerator, ErrorReporter errorReporter, Class<? extends Record> recordType) {
        this.csvFileLocation = csvFileLocation;
        this.outputGenerator = outputGenerator;
        this.errorReporter = errorReporter;
        this.recordType = recordType;
    }

    public void run() {
        try (
                CSVReader reader = new CSVReaderBuilder(new BufferedReader(new FileReader(this.csvFileLocation, StandardCharsets.UTF_8)))
                        .withCSVParser(new RFC4180ParserBuilder().build()).build()
        ) {

            String[] headers = reader.peek();

            if (headers == null) {
                throw new InvalidInputFileNoHeaderException();
            }

            for (String headerField : headers) {
                if (headerField.isEmpty()) {
                    throw new InvalidInputFileEmptyHeadersException();
                }
            }

            boolean hasDuplicates = Arrays.stream(headers).distinct().count() != headers.length;

            if (hasDuplicates) {
                throw new InvalidInputFileDuplicateHeadersException();
            }

            CsvToBean<Record> csvToBean = new CsvToBeanBuilder<Record>(reader)
                    .withType(this.recordType)
                    .withIgnoreEmptyLine(false)
                    .withThrowExceptions(false)
                    .withOrderedResults(true)
                    .withExceptionHandler(this.errorReporter)
                    .build();

            csvToBean.iterator().forEachRemaining(this.outputGenerator::apply);

            if (reader.getLinesRead() < 2) {
                throw new InvalidInputFileNoRecordException();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}