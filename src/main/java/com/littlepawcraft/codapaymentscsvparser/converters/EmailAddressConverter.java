package com.littlepawcraft.codapaymentscsvparser.converters;

import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidFieldValueEmailAddressException;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.validator.routines.EmailValidator;

public class EmailAddressConverter extends AbstractBeanField<String, String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(value)) {
            throw new InvalidFieldValueEmailAddressException(value);
        }
        return value;
    }
}
