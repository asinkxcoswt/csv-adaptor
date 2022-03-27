package com.littlepawcraft.csvadaptor.converters;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.littlepawcraft.csvadaptor.exceptions.InvalidFieldValuePhoneNumberException;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class PhoneNumberConverter extends AbstractBeanField<String, String> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(value, "TH");
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                throw new InvalidFieldValuePhoneNumberException(value);
            }
        } catch (NumberParseException e) {
            throw new InvalidFieldValuePhoneNumberException(value, e.getMessage());
        }

        return value;
    }
}
