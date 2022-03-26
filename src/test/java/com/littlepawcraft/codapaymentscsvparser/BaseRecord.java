package com.littlepawcraft.codapaymentscsvparser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindAndJoinByName;
import org.apache.commons.collections4.MultiValuedMap;

public abstract class BaseRecord {
    @JsonProperty
    @CsvBindAndJoinByName(column = ".*", elementType = String.class)
    protected MultiValuedMap<String, String> theRest;
}
