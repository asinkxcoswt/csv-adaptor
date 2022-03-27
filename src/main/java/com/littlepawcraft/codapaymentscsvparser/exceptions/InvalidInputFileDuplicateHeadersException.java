package com.littlepawcraft.codapaymentscsvparser.exceptions;

public class InvalidInputFileDuplicateHeadersException extends RuntimeException {
    public InvalidInputFileDuplicateHeadersException() {
        super("File has duplicate headers");
    }
}
