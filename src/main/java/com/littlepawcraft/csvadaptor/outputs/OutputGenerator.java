package com.littlepawcraft.csvadaptor.outputs;

import com.littlepawcraft.csvadaptor.records.Record;

public interface OutputGenerator {
    void apply(Record record);
}
