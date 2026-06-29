package br.com.gastrofactorapi.application.exceptions;

import org.springframework.http.HttpStatus;

public class ApplicationNotFoundException extends ApplicationBusinessException {

    private static final long serialVersionUID = 1L;

    public ApplicationNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}