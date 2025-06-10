package com.vgt.tournaments.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(EntityNotFoundException ex, WebRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setDetail(ex.getMessage());

    return new ResponseEntity<>(ErrorResponse.builder(ex, problemDetail).build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleResourceBadRequestException(IllegalArgumentException ex, WebRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setDetail(ex.getMessage());

    return new ResponseEntity<>(ErrorResponse.builder(ex, problemDetail).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleResourceConflictException(IllegalStateException ex, WebRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setDetail(ex.getMessage());

    return new ResponseEntity<>(ErrorResponse.builder(ex, problemDetail).build(),
        HttpStatus.CONFLICT);
  }
}
