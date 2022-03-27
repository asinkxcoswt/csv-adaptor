package com.littlepawcraft.codapaymentscsvparser.integrations;

import com.littlepawcraft.codapaymentscsvparser.CodapaymentsCsvParserApplication;
import com.littlepawcraft.codapaymentscsvparser.utils.FileTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {
        "input-file=" + SmokeIT.INPUT_FILE,
        "output-dir=" + SmokeIT.OUTPUT_DIR,
        "record-type=example",
        "output-format=json"
})
public class SmokeIT {

    public static final String INPUT_FILE = "src/test/resources/integration-test-inputs/example.csv";
    public static final String OUTPUT_DIR = "target/integration-test-outputs";

    @Autowired
    CodapaymentsCsvParserApplication codapaymentsCsvParserApplication;

    @Test
    void line2ShouldBeGeneratedSuccessfully() {
        String line2Output = FileTestUtil.loadFileAsString(OUTPUT_DIR + "/2.out.json");
        FileTestUtil.checkJson(line2Output, """
                {
                  "foo" : "foo1",
                  "desc" : "valid",
                  "stringField" : "hello",
                  "integerNumber" : 1,
                  "phoneField" : "+660999999999",
                  "emailField" : "john@example.com"
                }
                """);
    }

    @Test
    void line3ShouldBeGeneratedSuccessfully() {
        String line3Output = FileTestUtil.loadFileAsString(OUTPUT_DIR + "/3.out.json");
        FileTestUtil.checkJson(line3Output, """
                {
                  "foo" : "foo2",
                  "desc" : "valid",
                  "stringField" : "hello",
                  "integerNumber" : 1,
                  "phoneField" : "+660999999999",
                  "emailField" : "john@example.com"
                }
                """);
    }

    @Test
    void line4ShouldHaveErrorParsingEmail() {
        String line4Output = FileTestUtil.loadFileAsString(OUTPUT_DIR + "/4.err.txt");
        assertThat(line4Output).isEqualTo("The value invalid@example.co.th.xx.yy is not valid for Email Address");
    }

    @Test
    void line5ShouldHaveErrorParsingPhoneNumber() {
        String line5Output = FileTestUtil.loadFileAsString(OUTPUT_DIR + "/5.err.txt");
        assertThat(line5Output).isEqualTo("The string supplied did not seem to be a phone number.");
    }

    @Test
    void line6ShouldHaveErrorParsingNumber() {
        String line6Output = FileTestUtil.loadFileAsString(OUTPUT_DIR + "/6.err.txt");
        assertThat(line6Output).isEqualTo("Conversion of abc to java.lang.Integer failed.");
    }
}
