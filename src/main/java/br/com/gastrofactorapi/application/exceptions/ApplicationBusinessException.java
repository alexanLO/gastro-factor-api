package br.com.gastrofactorapi.application.exceptions;

public class ApplicationBusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int statusCode;

    public ApplicationBusinessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}