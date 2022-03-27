package com.littlepawcraft.codapaymentscsvparser.exceptions;

public class InvalidInputFileNoHeaderException extends RuntimeException {
    public InvalidInputFileNoHeaderException() {
        super("File has no header");
    }
}
