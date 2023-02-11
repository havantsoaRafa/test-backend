package com.maiia.pro.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.maiia.pro.dtos.ErrorDTO;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private final boolean stacktraceEnabled;

  public RestExceptionHandler(
      @Value("${logging.stacktraceEnabled:true}") boolean stacktraceEnabled) {
    this.stacktraceEnabled = stacktraceEnabled;
  }


  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorDTO> handleValidationException(RuntimeException exception) {
    log.error(getMessage(exception, BAD_REQUEST.getReasonPhrase() + ": " + exception.getMessage()));

    var errorRepresentation =
        ErrorDTO.builder()
            .errorCode(String.valueOf(BAD_REQUEST.value()))
            .errorMessage(exception.getMessage())
            .errorLevel("error")
            .errorType("functional")
            .build();
    return ResponseEntity.badRequest().body(errorRepresentation);
  }



  private String getMessage(Throwable throwable, String message) {
    return stacktraceEnabled ? getStacktrace(throwable) : message;
  }

  private static String getStacktrace(Throwable throwable) {
    var sw = new StringWriter();
    var pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    return sw.getBuffer().toString();
  }

}
