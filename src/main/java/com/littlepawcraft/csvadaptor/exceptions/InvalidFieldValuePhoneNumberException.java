package com.littlepawcraft.csvadaptor.exceptions;

import com.opencsv.exceptions.CsvConstraintViolationException;

public class InvalidFieldValuePhoneNumberException extends CsvConstraintViolationException {
    public InvalidFieldValuePhoneNumberException(String value) {
        this(value, "The value " + value + " is not valid for Phone Number");
    }

    public InvalidFieldValuePhoneNumberException(String value, String message) {
        super(value, message);
    }
}
