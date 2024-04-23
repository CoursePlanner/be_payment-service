package org.course_planner.utils.advice;

import org.course_planner.utils.dto.ExceptionResponseDTO;
import org.course_planner.utils.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CPExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<ExceptionResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {MessageException.class})
    protected ResponseEntity<ExceptionResponseDTO> handleMessageException(MessageException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {PrerequisiteFailureException.class})
    protected ResponseEntity<ExceptionResponseDTO> handlePrerequisiteFailureException(PrerequisiteFailureException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {PropertyNotFoundException.class})
    protected ResponseEntity<ExceptionResponseDTO> handlePropertyNotFoundException(PropertyNotFoundException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {UserException.class})
    protected ResponseEntity<ExceptionResponseDTO> handleUserException(UserException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {APIExecutionException.class})
    protected ResponseEntity<ExceptionResponseDTO> handleAPIExecutionException(APIExecutionException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<ExceptionResponseDTO> handleAuthorizationException(AuthorizationException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {UnhandledException.class})
    protected ResponseEntity<ExceptionResponseDTO> handleAuthorizationException(UnhandledException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {PaymentException.class})
    protected ResponseEntity<ExceptionResponseDTO> handlePaymentException(PaymentException ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(ex);
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ExceptionResponseDTO> handleAllExceptions(Exception ex) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO();
        responseDTO.setExceptionType(ex.getClass().getName());
        responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setErrorMessage(ex.getMessage());
        responseDTO.setThrowable(ex);
        responseDTO.setTimestamp(LocalDateTime.now());
        if (ex.getCause() != null) {
            responseDTO.setError(ex.getCause().getMessage());
        } else {
            responseDTO.setError(ex.getMessage());
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }
}
