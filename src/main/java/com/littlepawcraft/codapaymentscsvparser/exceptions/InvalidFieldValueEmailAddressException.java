package com.littlepawcraft.codapaymentscsvparser.exceptions;

import com.opencsv.exceptions.CsvConstraintViolationException;

public class InvalidFieldValueEmailAddressException extends CsvConstraintViolationException {
    public InvalidFieldValueEmailAddressException(String value) {
        super(value, "The value " + value + " is not valid for Email Address");
    }
}
