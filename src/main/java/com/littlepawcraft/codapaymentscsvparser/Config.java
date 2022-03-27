package com.littlepawcraft.codapaymentscsvparser;

import com.littlepawcraft.codapaymentscsvparser.records.ExampleRecord;
import com.littlepawcraft.codapaymentscsvparser.records.NoFieldsRecord;
import com.littlepawcraft.codapaymentscsvparser.records.RecordTypeRegistry;
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
