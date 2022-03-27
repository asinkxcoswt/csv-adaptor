package com.littlepawcraft.csvadaptor.exceptions;

public class InvalidInputFileEmptyHeadersException extends RuntimeException {
    public InvalidInputFileEmptyHeadersException() {
        super("File has some empty headers");
    }
}
