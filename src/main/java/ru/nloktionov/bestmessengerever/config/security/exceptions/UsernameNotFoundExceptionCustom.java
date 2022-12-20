package ru.nloktionov.bestmessengerever.config.security.exceptions;

public class UsernameNotFoundExceptionCustom extends RuntimeException {
    public UsernameNotFoundExceptionCustom(String message) {
        super(message);
    }
}
