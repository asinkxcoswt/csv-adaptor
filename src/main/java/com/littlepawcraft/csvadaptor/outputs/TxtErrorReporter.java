package com.littlepawcraft.csvadaptor.outputs;

import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public class TxtErrorReporter implements ErrorReporter {

    private final String outputDirectory;

    public TxtErrorReporter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public CsvException handleException(CsvException e) throws CsvException {
        File outFile = new File(this.outputDirectory + "/" + e.getLineNumber() + ".err.txt");
        new File(outFile.getParent()).mkdirs();
        try {
            Files.writeString(outFile.toPath(), e.getMessage());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return e;
    }
}
