package com.littlepawcraft.codapaymentscsvparser;

import com.littlepawcraft.codapaymentscsvparser.inputs.CsvProcessor;
import com.littlepawcraft.codapaymentscsvparser.outputs.*;
import com.littlepawcraft.codapaymentscsvparser.records.Record;
import com.littlepawcraft.codapaymentscsvparser.records.RecordTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodapaymentsCsvParserApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(CodapaymentsCsvParserApplication.class);

	@Value("${input-file:}")
	private String inputFile;

	@Value("${output-dir:}")
	private String outputDir;

	@Value("${record-type:default}")
	private String recordType;

	@Value("${output-format:json}")
	private String outputFormat;

	@Autowired
	private RecordTypeRegistry recordTypeRegistry;

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(CodapaymentsCsvParserApplication.class, args);
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

		csvProcessor.run();
	}
}
