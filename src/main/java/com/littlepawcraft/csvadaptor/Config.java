package com.littlepawcraft.csvadaptor;

import com.littlepawcraft.csvadaptor.records.ExampleRecord;
import com.littlepawcraft.csvadaptor.records.NoFieldsRecord;
import com.littlepawcraft.csvadaptor.records.RecordTypeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    RecordTypeRegistry recordTypeRegistry() {
        return new RecordTypeRegistry()
                .add("default", NoFieldsRecord.class)
                .add("example", ExampleRecord.class);
    }
}
