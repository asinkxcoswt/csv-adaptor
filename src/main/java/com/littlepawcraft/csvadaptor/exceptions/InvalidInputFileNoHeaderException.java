package com.littlepawcraft.csvadaptor.exceptions;

public class InvalidInputFileNoHeaderException extends RuntimeException {
    public InvalidInputFileNoHeaderException() {
        super("File has no header");
    }
}
