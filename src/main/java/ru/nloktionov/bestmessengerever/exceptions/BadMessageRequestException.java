package ru.nloktionov.bestmessengerever.exceptions;

public class BadMessageRequestException extends RuntimeException {
    public BadMessageRequestException(String message) {
        super(message);
    }
}
