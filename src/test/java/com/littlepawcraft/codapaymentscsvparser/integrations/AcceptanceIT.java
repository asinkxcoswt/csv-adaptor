package com.littlepawcraft.codapaymentscsvparser.integrations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AcceptanceIT {
    @Test
    @DisplayName("A user must be able to run the program and feed it a location to file that has CSV data")
    void canFeedInputFile() {
    }

    @Test
    @DisplayName("The system must be able to parse each line of CSV into a record of data")
    void canParseCsvToRecord() {}

    @Test
    @DisplayName("The system must output the formatted record to a separate text file for each record (JSON)")
    void canGenerateJsonOutputForEachRecord() {}

    @Test
    @DisplayName("The system must output the formatted record to a separate text file for each record (XML)")
    void canGenerateXmlOutputForEachRecord() {}

    @Test
    @DisplayName("Each formatted record should include the name and value for each of the record's fields")
    void outputShouldHasAllFields() {
    }

    @Test
    @DisplayName("Extend the formatted record to also include either the String or Number data type for each of the record's fields")
    void outputShouldConvertSupportedStringAndNumberFields() {
    }

    @Test
    @DisplayName("Extend the formatted record to also include the Phone number data types (case valid phone number)")
    void outputCanConvertValidPhoneNumber() {
    }

    @Test
    @DisplayName("Extend the formatted record to also include the Phone number data types (case invalid phone number)")
    void outputCanRejectInvalidPhoneNumber() {}

    @Test
    @DisplayName("Extend the formatted record to also include the Email address data types (case valid email address)")
    void outputCanConvertValidEmailAddress() {}

    @Test
    @DisplayName("Extend the formatted record to also include the Email address data types (case invalid email address)")
    void outputCanRejectInvalidEmailAddress() {}

}
