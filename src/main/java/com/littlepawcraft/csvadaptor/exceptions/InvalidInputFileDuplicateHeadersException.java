package com.littlepawcraft.csvadaptor.exceptions;

public class InvalidInputFileDuplicateHeadersException extends RuntimeException {
    public InvalidInputFileDuplicateHeadersException() {
        super("File has duplicate headers");
    }
}
