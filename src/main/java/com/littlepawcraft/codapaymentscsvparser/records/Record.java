package com.littlepawcraft.codapaymentscsvparser.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.littlepawcraft.codapaymentscsvparser.records.serializers.TheRestFieldsSerializer;
import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindLineNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

@Getter
@Setter
@ToString

public abstract class Record {

    @JsonIgnore
    @CsvBindLineNumber
    protected long lineNumber;

    @JsonUnwrapped
    @JsonSerialize(using = TheRestFieldsSerializer.class)
    @CsvBindAndJoinByName(column = ".*", elementType = String.class)
    protected MultiValuedMap<String, String> theRest = new HashSetValuedHashMap<>();
}
