package br.com.gastrofactorapi.infrastructure.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.gastrofactorapi.application.exceptions.ApplicationBusinessException;
import br.com.gastrofactorapi.infrastructure.exceptions.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        ApiErrorResponse error = new ApiErrorResponse(HttpStatus.BAD_REQUEST);
        error.addValidationErrors(ex.getFieldErrors());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {

        HttpStatus status;
        try {
            status = HttpStatus.resolve(Integer.parseInt(ex.getErroCode()));
        } catch (NumberFormatException numberFormatException) {
            status = HttpStatus.BAD_REQUEST;
        }

        status = status != null ? status : HttpStatus.BAD_REQUEST;

        var apiErro = new ApiErrorResponse(status, ex);
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(ApplicationBusinessException.class)
    public ResponseEntity<Object> handleApplicationBusinessException(ApplicationBusinessException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
        status = status != null ? status : HttpStatus.BAD_REQUEST;

        var apiErro = new ApiErrorResponse(status, ex);
        return buildResponseEntity(apiErro, ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiError, Exception ex) {
        log.error("Excecao sendo capturada, APIErrorCode: {}, Menssagem: {}, Excecao ", apiError.getCodeError(),
                apiError.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
