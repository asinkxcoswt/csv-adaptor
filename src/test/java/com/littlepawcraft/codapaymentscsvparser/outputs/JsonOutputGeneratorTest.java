package com.littlepawcraft.codapaymentscsvparser.outputs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.littlepawcraft.codapaymentscsvparser.records.ExampleRecord;
import com.littlepawcraft.codapaymentscsvparser.records.Record;
import com.littlepawcraft.codapaymentscsvparser.utils.FileTestUtil;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.junit.jupiter.api.Test;

class JsonOutputGeneratorTest {

    private record Sut(JsonOutputGenerator jsonOutputGenerator, String testResultDirectory) {}
    Sut useSut(String testResultLocalPath) {
        String testResultDirectory = FileTestUtil.toAbsolutePath("test-result/JsonOutputGeneratorTest/" + testResultLocalPath);
        JsonOutputGenerator jsonOutputGenerator = new JsonOutputGenerator(testResultDirectory);
        return new Sut(jsonOutputGenerator, testResultDirectory);
    }

    Record makeRecord(long lineNumber) {
        ExampleRecord record = new ExampleRecord();
        record.setLineNumber(lineNumber);
        record.setEmailField("john@example.com");
        record.setIntegerNumber(1);
        record.setPhoneField("+660999999999");
        record.setStringField("Hello");

        MultiValuedMap<String, String> theRest = new HashSetValuedHashMap<>();
        theRest.put("foo", "fooValue");
        theRest.put("bar", "barValue");
        record.setTheRest(theRest);
        return record;
    }



    @Test
    void canWriteARecordToJsonFile() throws JsonProcessingException {
        Record record = makeRecord(1);
        Sut sut = useSut("canWriteARecordToJsonFile");

        sut.jsonOutputGenerator.apply(record);

        String outputContent = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/1.out.json");
        FileTestUtil.checkJson(outputContent, """
                {
                "bar" : "barValue",
                "foo" : "fooValue",
                "stringField" : "Hello",
                "integerNumber" : 1,
                "phoneField" : "+660999999999",
                "emailField" : "john@example.com"
                }
        """);
    }

    @Test
    void canWriteMultipleRecordsToSeparatedJsonFileWithCorrectFileName() throws JsonProcessingException {
        Record record1 = makeRecord(1);
        Record record2 = makeRecord(2);

        Sut sut = useSut("canWriteMultipleRecordsToSeparatedJsonFileWithCorrectFileName");

        sut.jsonOutputGenerator.apply(record1);
        sut.jsonOutputGenerator.apply(record2);

        String outputContent1 = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/1.out.json");
        String outputContent2 = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/2.out.json");

        FileTestUtil.checkJson(outputContent1, """
                {
                "bar" : "barValue",
                "foo" : "fooValue",
                "stringField" : "Hello",
                "integerNumber" : 1,
                "phoneField" : "+660999999999",
                "emailField" : "john@example.com"
                }
        """);
        FileTestUtil.checkJson(outputContent2, """
                {
                "bar" : "barValue",
                "foo" : "fooValue",
                "stringField" : "Hello",
                "integerNumber" : 1,
                "phoneField" : "+660999999999",
                "emailField" : "john@example.com"
                }
        """);
    }
}