package com.littlepawcraft.codapaymentscsvparser.exceptions;

public class InvalidInputFileEmptyHeadersException extends RuntimeException {
    public InvalidInputFileEmptyHeadersException() {
        super("File has some empty headers");
    }
}
