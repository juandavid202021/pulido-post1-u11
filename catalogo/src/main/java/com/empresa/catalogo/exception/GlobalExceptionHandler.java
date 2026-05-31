package com.empresa.catalogo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — Recurso no encontrado
    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(RecursoNoEncontradoException ex,
                                   HttpServletRequest req) {
        return new ApiError(404, "Not Found",
                ex.getMessage(), req.getRequestURI());
    }

    // 400 — Validacion fallida (@Valid en el controlador)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex,
                                     HttpServletRequest req) {
        String errores = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return new ApiError(400, "Bad Request",
                errores, req.getRequestURI());
    }

    // 500 — Error generico inesperado
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGeneral(Exception ex, HttpServletRequest req) {
        return new ApiError(500, "Internal Server Error",
                "Error inesperado. Contactar soporte.",
                req.getRequestURI());
    }
}