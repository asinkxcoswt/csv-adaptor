package com.littlepawcraft.csvadaptor;

import com.littlepawcraft.csvadaptor.inputs.CsvProcessor;
import com.littlepawcraft.csvadaptor.outputs.*;
import com.littlepawcraft.csvadaptor.records.Record;
import com.littlepawcraft.csvadaptor.records.RecordTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
public class CsvAdaptorApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(CsvAdaptorApplication.class);

	@Value("${input-file:}")
	private String inputFile;

	@Value("${output-dir:}")
	private String outputDir;

	@Value("${record-type:default}")
	private String recordType;

	@Value("${output-format:json}")
	private String outputFormat;

	@Value("${parallel:false}")
	private boolean parallel;

	@Autowired
	private RecordTypeRegistry recordTypeRegistry;

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		Instant startTime = Instant.now();
		SpringApplication.run(CsvAdaptorApplication.class, args);
		Instant endTime = Instant.now();
		LOG.info("Elapse (seconds): " + ChronoUnit.SECONDS.between(startTime, endTime));
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("inputFile: " + this.inputFile);
		LOG.info("outputDir: " + this.outputDir);
		LOG.info("outputFormat: " + this.outputFormat);
		LOG.info("recordType: " + this.recordType);

		if (this.inputFile.isEmpty()) {
			throw new RuntimeException("Missing value for parameter --input-file");
		}

		if (this.outputDir.isEmpty()) {
			throw new RuntimeException("Missing value for parameter --output-dir");
		}

		if (this.outputFormat.isEmpty()) {
			throw new RuntimeException("Missing value for parameter --output-format");
		}

		if (this.recordType.isEmpty()) {
			throw new RuntimeException("Missing value for parameter --record-type");
		}

		Class<? extends Record> recordTypeClass = recordTypeRegistry.get(this.recordType)
				.orElseThrow(() -> new RuntimeException("Unsupported record type: " + this.recordType));

		OutputGenerator outputGenerator = switch (this.outputFormat) {
			case "json" -> new JsonOutputGenerator(this.outputDir);
			case "xml" -> new XmlOutputGenerator(this.outputDir);
			default -> throw new RuntimeException("Unsupported output format: " + this.outputFormat);
		};

		ErrorReporter errorReporter = new TxtErrorReporter(this.outputDir);

		CsvProcessor csvProcessor = new CsvProcessor(this.inputFile, outputGenerator, errorReporter, recordTypeClass);
		csvProcessor.setParallel(this.parallel);

		csvProcessor.run();
	}
}
