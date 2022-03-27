package com.littlepawcraft.codapaymentscsvparser.outputs;

import com.littlepawcraft.codapaymentscsvparser.records.Record;

public interface OutputGenerator {
    void apply(Record record);
}
