package com.littlepawcraft.codapaymentscsvparser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import org.apache.commons.collections4.MultiValuedMap;

public class Record extends BaseRecord {

    @JsonProperty
    @CsvBindByName
    private String foo;

    @Override
    public String toString() {
        return "Record{" +
                "foo='" + foo + '\'' +
                ", theRest=" + theRest +
                '}';
    }
}
