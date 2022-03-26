package com.littlepawcraft.codapaymentscsvparser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Stream;

public class FooTest {

    @Test
    public void test() throws Exception {

        String collection = "bank1";

        /**
         * collections.CollectionResolver
         * collections.BaseRecord
         * collections.Bank1
         * collections.Bank2
         * collections.Bank3
         *
         * converters.EmailAddress
         * converters.PhoneNumber
         * converters.Number
         * converters.String
         */
        // Class<?> clazz = resolve(collection);
        Class<?> collectionClass = Record.class;


        /**
         * inputs.RecordsProvider
         */
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("file.csv")).withCSVParser(new RFC4180ParserBuilder().build()).build()) {

            String[] headers = reader.peek();
            System.out.println(Arrays.toString(headers));


            CsvToBean<?> csvToBean = new CsvToBeanBuilder<>(reader)
                    .withType(collectionClass)
                    .withIgnoreEmptyLine(false)
                    .withThrowExceptions(false)
                    .withOrderedResults(true)
                    .withExceptionHandler(new CsvExceptionHandler() {
                        /**
                         * outputs.ErrorReporter
                         */
                        @Override
                        public CsvException handleException(CsvException e) throws CsvException {
                            System.out.println(e.getLineNumber() + " : " + e.getMessage() + " -- " + Arrays.toString(e.getLine()));
                            return e;
                        }
                    })
                    .build();

            csvToBean.iterator().forEachRemaining(record -> {
//                System.out.println("xxx read: " + reader.getRecordsRead());
                /**
                 * outputs.JsonOutputGenerator
                 */
                try {
                    System.out.println(reader.getRecordsRead() + " : " + new ObjectMapper().writeValueAsString(record));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            });
//
            System.out.println("Record read: " + reader.getRecordsRead());
            System.out.println("Line read: " + reader.getLinesRead());

//            System.out.println(Arrays.toString(csvToBean.getCapturedExceptions().toArray()));
                System.out.println(Arrays.toString(csvToBean.getCapturedExceptions().toArray()));
        }
    }
}
