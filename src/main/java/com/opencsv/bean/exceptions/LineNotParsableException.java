package com.opencsv.bean.exceptions;

import com.opencsv.exceptions.CsvException;

public class LineNotParsableException extends CsvException {
    public LineNotParsableException(String message) {
        super(message);
    }
}
