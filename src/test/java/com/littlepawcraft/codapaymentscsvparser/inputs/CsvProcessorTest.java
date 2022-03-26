package com.littlepawcraft.codapaymentscsvparser.inputs;

import static org.junit.jupiter.api.Assertions.*;

class CsvProcessorTest {
    void canRejectInvalidRecordIncorrectNumberOfFields() {}
    void canRejectInvalidRecordFailDataConversion() {}
    void canRejectInvalidRecordNotParsable() {}
    void canSkipInvalidRecord() {}
    void canContinueProcessAfterSkipInvalidRecord() {}
    void canReportErrors() {}

    void canRejectFileNoHeader() {}
    void canRejectFileNoRecord() {}
    void canRejectFileHeaderDuplicate() {}
    void canRejectFileHeaderEmpty() {}
    void canRejectFileHeaderNotParsable() {}

    void canParseQuotedFields() {}
    void canParseEscapedCharacters() {}
}