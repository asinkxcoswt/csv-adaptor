package com.littlepawcraft.csvadaptor.records;

import com.littlepawcraft.csvadaptor.converters.EmailAddressConverter;
import com.littlepawcraft.csvadaptor.converters.PhoneNumberConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ExampleRecord extends Record {

    @CsvBindByName
    private String stringField;

    @CsvBindByName
    private Integer integerNumber;

    @CsvBindByName
    private Long longNumber;

    @CsvBindByName
    private Short shortNumber;

    @CsvBindByName
    private Byte byteNumber;

    @CsvCustomBindByName(converter = PhoneNumberConverter.class)
    private String phoneField;

    @CsvCustomBindByName(converter = EmailAddressConverter.class)
    private String emailField;
}
