package com.littlepawcraft.codapaymentscsvparser.inputs;

import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileDuplicateHeadersException;
import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileEmptyHeadersException;
import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileNoHeaderException;
import com.littlepawcraft.codapaymentscsvparser.exceptions.InvalidInputFileNoRecordException;
import com.littlepawcraft.codapaymentscsvparser.outputs.ErrorReporter;
import com.littlepawcraft.codapaymentscsvparser.outputs.OutputGenerator;
import com.littlepawcraft.codapaymentscsvparser.records.ExampleRecord;
import com.littlepawcraft.codapaymentscsvparser.records.NoFieldsRecord;
import com.littlepawcraft.codapaymentscsvparser.records.Record;
import com.littlepawcraft.codapaymentscsvparser.utils.FileTestUtil;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;

class CsvProcessorTest {

    private record Sut(CsvProcessor csvProcessor, OutputGenerator outputGenerator, ErrorReporter errorReporter) {}
    private static Sut useSut(String csvResourcePath, Class<? extends Record> recordType) throws CsvException {
        String csvFileLocation = FileTestUtil.toAbsolutePath( csvResourcePath);
        OutputGenerator outputGenerator = Mockito.mock(OutputGenerator.class);
        ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
        CsvProcessor csvProcessor = new CsvProcessor(csvFileLocation, outputGenerator, errorReporter, recordType);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            CsvException csvException = (CsvException) invocationOnMock.getArguments()[0];
            throw csvException;
        }).when(errorReporter).handleException(Mockito.any(CsvException.class));
        return new Sut(csvProcessor, outputGenerator, errorReporter);
    }

    private void checkUnsupportedFieldValue(Record record, String field, String expectedValue) {
        String value = record.getTheRest().get(field).stream().findFirst().orElseGet(() -> fail("Expect value of " + field + " but got null"));
        assertThat(value).isEqualTo(expectedValue);
    }

    @Test
    void canReadSimpleValidCsv() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canReadSimpleValidCsv.csv", NoFieldsRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            NoFieldsRecord record = (NoFieldsRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(2L, 3L);
            checkUnsupportedFieldValue(record, "foo", "fooValue");
            checkUnsupportedFieldValue(record, "bar", "barValue");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(2)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canParseNumberFields() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseNumberFields.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(2L);
            checkUnsupportedFieldValue(record, "foo", "fooValue");
            checkUnsupportedFieldValue(record, "bar", "barValue");
            assertThat(record.getByteNumber()).isEqualTo((byte) 1);
            assertThat(record.getShortNumber()).isEqualTo((short) 1);
            assertThat(record.getIntegerNumber()).isEqualTo(1);
            assertThat(record.getLongNumber()).isEqualTo(1);
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canParseStringField() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseStringField.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(2L);
            checkUnsupportedFieldValue(record, "foo", "fooValue");
            checkUnsupportedFieldValue(record, "bar", "barValue");
            assertThat(record.getStringField()).isEqualTo("this is a string");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canParseEmptyString() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseEmptyString.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(2L);
            checkUnsupportedFieldValue(record, "foo", "fooValue");
            checkUnsupportedFieldValue(record, "bar", "barValue");
            assertThat(record.getStringField()).isEqualTo("");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canParseValidPhoneNumberField() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseValidPhoneNumberField.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(2L);
            checkUnsupportedFieldValue(record, "foo", "fooValue");
            checkUnsupportedFieldValue(record, "bar", "barValue");
            assertThat(record.getPhoneField()).isEqualTo("+660999999999");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canRejectInvalidPhoneNumber() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectInvalidPhoneNumber.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            CsvException csvException = (CsvException) invocationOnMock.getArguments()[0];
            assertThat(csvException.getLineNumber()).isEqualTo(2);
            assertThat(csvException.getMessage()).isEqualTo("The value +6609999999xx is not valid for Phone Number");
            return null;
        }).when(sut.errorReporter).handleException(Mockito.any(CsvException.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.errorReporter, Mockito.times(1)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canParseEmailAddressField() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseEmailAddressField.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(2L);
            checkUnsupportedFieldValue(record, "foo", "fooValue");
            checkUnsupportedFieldValue(record, "bar", "barValue");
            assertThat(record.getEmailField()).isEqualTo("john@example.co.th");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canRejectInvalidEmailAddress() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectInvalidEmailAddress.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            CsvException csvException = (CsvException) invocationOnMock.getArguments()[0];
            assertThat(csvException.getLineNumber()).isEqualTo(2);
            assertThat(csvException.getMessage()).isEqualTo("The value john@example.co.th.xx.yy is not valid for Email Address");
            return null;
        }).when(sut.errorReporter).handleException(Mockito.any(CsvException.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
        Mockito.verify(sut.errorReporter, Mockito.times(1)).handleException(Mockito.any(CsvException.class));
    }

    @Test
    void canRejectInvalidRecordIncorrectNumberOfFields() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectInvalidRecordIncorrectNumberOfFields.csv", NoFieldsRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            CsvException csvException = (CsvException) invocationOnMock.getArguments()[0];
            assertThat(csvException.getLineNumber()).isEqualTo(2);
            assertThat(csvException.getMessage()).isEqualTo("Number of data fields does not match number of headers.");
            return null;
        }).when(sut.errorReporter).handleException(Mockito.any(CsvException.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.errorReporter, Mockito.times(1)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectInvalidRecordFailDataConversion() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectInvalidRecordFailDataConversion.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            CsvException csvException = (CsvException) invocationOnMock.getArguments()[0];
            assertThat(csvException.getLineNumber()).isEqualTo(2);
            assertThat(csvException.getMessage()).isEqualTo("Conversion of hello to java.lang.Integer failed.");
            return null;
        }).when(sut.errorReporter).handleException(Mockito.any(CsvException.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.errorReporter, Mockito.times(1)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectInvalidRecordNotParsable() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectInvalidRecordNotParsable.csv", NoFieldsRecord.class);

        assertThatThrownBy(sut.csvProcessor::run).isInstanceOf(RuntimeException.class);
    }

    @Test
    void canSkipInvalidRecordAndContinueProcessNextLines() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canSkipInvalidRecordAndContinueProcessNextLines.csv", ExampleRecord.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            CsvException csvException = (CsvException) invocationOnMock.getArguments()[0];
            assertThat(csvException.getLineNumber()).isIn(2L, 3L, 5L);
            return null;
        }).when(sut.errorReporter).handleException(Mockito.any(CsvException.class));

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isIn(4L, 6L);
            checkUnsupportedFieldValue(record, "foo", "ok");
            checkUnsupportedFieldValue(record, "bar", "ok");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.errorReporter, Mockito.times(3)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(2)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectFileNoHeader() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectFileNoHeader.csv", ExampleRecord.class);

        assertThatThrownBy(sut.csvProcessor::run).isInstanceOf(InvalidInputFileNoHeaderException.class);

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectFileNoRecord() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectFileNoRecord.csv", ExampleRecord.class);

        assertThatThrownBy(sut.csvProcessor::run).isInstanceOf(InvalidInputFileNoRecordException.class);

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectFileHeaderDuplicate() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectFileHeaderDuplicate.csv", ExampleRecord.class);

        assertThatThrownBy(sut.csvProcessor::run).isInstanceOf(InvalidInputFileDuplicateHeadersException.class);

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectFileHeaderEmpty() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectFileHeaderEmpty.csv", ExampleRecord.class);

        assertThatThrownBy(sut.csvProcessor::run).isInstanceOf(InvalidInputFileEmptyHeadersException.class);

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canRejectFileHeaderNotParsable() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canRejectFileHeaderNotParsable.csv", NoFieldsRecord.class);

        assertThatThrownBy(sut.csvProcessor::run).isInstanceOf(RuntimeException.class);

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(0)).apply(Mockito.any(Record.class));
    }

    @Test
    void canParseQuotedFields() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseQuotedFields.csv", ExampleRecord.class);
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ExampleRecord record = (ExampleRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isEqualTo(2);
            assertThat(record.getIntegerNumber()).isEqualTo(1);
            checkUnsupportedFieldValue(record, "foo", "hello, world");
            checkUnsupportedFieldValue(record, "bar", " hello world");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
    }

    @Test
    void canParseEscapedCharacters() throws CsvException {
        Sut sut = useSut("csvs/CsvProcessorTest/canParseEscapedCharacters.csv", NoFieldsRecord.class);
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            NoFieldsRecord record = (NoFieldsRecord) invocationOnMock.getArguments()[0];
            assertThat(record.getLineNumber()).isEqualTo(2);
            checkUnsupportedFieldValue(record, "foo\"", "hello \" world");
            checkUnsupportedFieldValue(record, "bar", "ok");
            return null;
        }).when(sut.outputGenerator).apply(Mockito.any(Record.class));

        sut.csvProcessor.run();

        Mockito.verify(sut.errorReporter, Mockito.times(0)).handleException(Mockito.any(CsvException.class));
        Mockito.verify(sut.outputGenerator, Mockito.times(1)).apply(Mockito.any(Record.class));
    }
}