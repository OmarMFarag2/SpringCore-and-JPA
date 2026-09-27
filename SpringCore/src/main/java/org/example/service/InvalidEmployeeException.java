package org.example.service;
public class InvalidEmployeeException extends RuntimeException {

    public InvalidEmployeeException(String message) {
        super(message);
    }
}