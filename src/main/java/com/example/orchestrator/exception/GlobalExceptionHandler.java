package com.example.orchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import io.grpc.StatusRuntimeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        HttpStatus status = getHttpStatus(ex);
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", status.value());
        errors.put("error", status.getReasonPhrase());
        errors.put("message", ex.getMessage());
        errors.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errors, status);
    }

    private HttpStatus getHttpStatus(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof MissingServletRequestParameterException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof SomeCustomException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof UnauthorizedException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof ConflictException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof StatusRuntimeException) {
            return handleGrpcException((StatusRuntimeException) ex);
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

     private HttpStatus handleGrpcException(StatusRuntimeException ex) {
        switch (ex.getStatus().getCode()) {
            case ALREADY_EXISTS:
                return HttpStatus.CONFLICT;
            case NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case UNAUTHENTICATED:
                return HttpStatus.UNAUTHORIZED;
            case PERMISSION_DENIED:
                return HttpStatus.FORBIDDEN;
            case INVALID_ARGUMENT:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}