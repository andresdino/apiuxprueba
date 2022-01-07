package com.example.pruebatecnica.controller;

import com.example.pruebatecnica.dto.RespuestaError;
import com.example.pruebatecnica.service.exceptions.ObjetoNoExisteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ManejadorExcepcionesRestController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ObjetoNoExisteException.class)
  public ResponseEntity<RespuestaError> objetoNoExistente(ObjetoNoExisteException ex) {
    //TODO Log de errores
    var respuesta = new RespuestaError(ex.getMessage());
    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
  public ResponseEntity<RespuestaError> errorInternoDelServidor(Exception ex) {
    ex.printStackTrace();
    var respuesta = new RespuestaError("Ocurrio un error inesperado");
    return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException exception, @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      WebRequest request) {
    List<String> validationErrors = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());
    return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, validationErrors);
  }

  private ResponseEntity<Object> getExceptionResponseEntity(final HttpStatus status,
      List<String> errors) {
    List<RespuestaError> errores =
        !CollectionUtils.isEmpty(errors) ? errors.stream().map(RespuestaError::new)
            .collect(Collectors.toList())
            : Arrays.asList(new RespuestaError(status.getReasonPhrase()));
    return new ResponseEntity<>(errores, status);
  }

}