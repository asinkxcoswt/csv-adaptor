package com.littlepawcraft.csvadaptor.records;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RecordTypeRegistry {
    private Map<String, Class<? extends Record>> recordTypeMap = new HashMap<>();

    public RecordTypeRegistry add(String recordTypeId, Class<? extends Record> recordType) {
        recordTypeMap.put(recordTypeId, recordType);
        return this;
    }

    public Optional<Class<? extends Record>> get(String recordTypeId) {
        return Optional.ofNullable(this.recordTypeMap.get(recordTypeId));
    }
}
