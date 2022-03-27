package com.littlepawcraft.csvadaptor.exceptions;

public class InvalidInputFileNoRecordException extends RuntimeException {
    public InvalidInputFileNoRecordException() {
        super("File has no record");
    }
}
