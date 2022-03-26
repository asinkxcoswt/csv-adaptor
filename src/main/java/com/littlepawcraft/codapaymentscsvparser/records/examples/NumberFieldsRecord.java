package com.littlepawcraft.codapaymentscsvparser.records.examples;

import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.bean.CsvConverter;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.Locale;

public class NumberFieldsRecord extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return null;
    }
}
