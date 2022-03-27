package com.littlepawcraft.codapaymentscsvparser.outputs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.littlepawcraft.codapaymentscsvparser.records.ExampleRecord;
import com.littlepawcraft.codapaymentscsvparser.records.Record;
import com.littlepawcraft.codapaymentscsvparser.utils.FileTestUtil;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.junit.jupiter.api.Test;

class XmlOutputGeneratorTest {

    private record Sut(XmlOutputGenerator xmlOutputGenerator, String testResultDirectory) {}
    Sut useSut(String testResultLocalPath) {
        String testResultDirectory = FileTestUtil.toAbsolutePath("test-result/XmlOutputGeneratorTest/" + testResultLocalPath);
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator(testResultDirectory);
        return new Sut(xmlOutputGenerator, testResultDirectory);
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
    void canWriteARecordToXmlFile() throws JsonProcessingException {
        Record record = makeRecord(1);
        Sut sut = useSut("canWriteARecordToXmlFile");

        sut.xmlOutputGenerator.apply(record);

        String outputContent = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/1.out.xml");

        FileTestUtil.checkXml(outputContent, """
                <MinimalIntegratedFieldsRecord>
                  <bar>barValue</bar>
                  <foo>fooValue</foo>
                  <stringField>Hello</stringField>
                  <integerNumber>1</integerNumber>
                  <phoneField>+660999999999</phoneField>
                  <emailField>john@example.com</emailField>
                </MinimalIntegratedFieldsRecord>
                """);
    }

    @Test
    void canWriteMultipleRecordsToSeparatedXmlFileWithCorrectFileName() throws JsonProcessingException {
        Record record1 = makeRecord(1);
        Record record2 = makeRecord(2);

        Sut sut = useSut("canWriteMultipleRecordsToSeparatedXmlFileWithCorrectFileName");

        sut.xmlOutputGenerator.apply(record1);
        sut.xmlOutputGenerator.apply(record2);

        String outputContent1 = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/1.out.xml");
        String outputContent2 = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/2.out.xml");

        FileTestUtil.checkXml(outputContent1, """
                <MinimalIntegratedFieldsRecord>
                  <bar>barValue</bar>
                  <foo>fooValue</foo>
                  <stringField>Hello</stringField>
                  <integerNumber>1</integerNumber>
                  <phoneField>+660999999999</phoneField>
                  <emailField>john@example.com</emailField>
                </MinimalIntegratedFieldsRecord>
                """);
        FileTestUtil.checkXml(outputContent2, """
                <MinimalIntegratedFieldsRecord>
                  <bar>barValue</bar>
                  <foo>fooValue</foo>
                  <stringField>Hello</stringField>
                  <integerNumber>1</integerNumber>
                  <phoneField>+660999999999</phoneField>
                  <emailField>john@example.com</emailField>
                </MinimalIntegratedFieldsRecord>
                """);
    }
}