package com.sample.poc.presentation.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {

  ErrorResponse errorResponse = ErrorResponse.builder()
    .timestamp(LocalDateTime.now())
    .status(HttpStatus.NOT_FOUND.value())
    .error("Não Encontrado")
    .message(ex.getMessage())
    .path(request.getDescription(false).replace("uri=", ""))
    .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
      EmailAlreadyExistsException ex, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error("Conflito")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(
      UnauthorizedException ex, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.UNAUTHORIZED.value())
      .error("Não Autorizado")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Falha de Validação")
      .message("Parâmetros de entrada inválidos")
      .validationErrors(errors)
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(
      RuntimeException ex, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Requisição Inválida")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      IllegalArgumentException ex, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .error("Requisição Inválida")
      .message(ex.getMessage())
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
      Exception ex, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("Erro Interno do Servidor")
      .message("Ocorreu um erro inesperado")
      .path(request.getDescription(false).replace("uri=", ""))
      .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}