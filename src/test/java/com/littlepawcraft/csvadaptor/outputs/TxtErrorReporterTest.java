package com.littlepawcraft.csvadaptor.outputs;

import com.littlepawcraft.csvadaptor.utils.FileTestUtil;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TxtErrorReporterTest {

    private record Sut(TxtErrorReporter txtErrorReporter, String testResultDirectory) {}
    Sut useSut(String testResultLocalPath) {
        String testResultDirectory = FileTestUtil.toAbsolutePath("test-result/TxtErrorReporterTest/" + testResultLocalPath);
        TxtErrorReporter txtErrorReporter = new TxtErrorReporter(testResultDirectory);
        return new Sut(txtErrorReporter, testResultDirectory);
    }

    @Test
    void canWriteErrorToTxtFile() throws CsvException {
        Sut sut = useSut("canWriteErrorToTxtFile");

        CsvException csvException = new CsvException("Test Error");
        csvException.setLineNumber(1);
        sut.txtErrorReporter.handleException(csvException);

        String errorContent = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/1.err.txt");
        assertThat(errorContent).isEqualTo("Test Error");
    }

    @Test
    void canWriteMultipleErrorToSeparatedTxtFiles() throws CsvException {
        Sut sut = useSut("canWriteMultipleErrorToSeparatedTxtFiles");

        CsvException csvException1 = new CsvException("Test Error 1");
        csvException1.setLineNumber(1);
        sut.txtErrorReporter.handleException(csvException1);

        CsvException csvException2 = new CsvException("Test Error 2");
        csvException2.setLineNumber(2);
        sut.txtErrorReporter.handleException(csvException2);

        String errorContent1 = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/1.err.txt");
        String errorContent2 = FileTestUtil.loadFileAsString(sut.testResultDirectory + "/2.err.txt");
        assertThat(errorContent1).isEqualTo("Test Error 1");
        assertThat(errorContent2).isEqualTo("Test Error 2");
    }
}