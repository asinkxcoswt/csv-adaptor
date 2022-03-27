package com.littlepawcraft.codapaymentscsvparser.exceptions;

public class InvalidInputFileNoRecordException extends RuntimeException {
    public InvalidInputFileNoRecordException() {
        super("File has no record");
    }
}
