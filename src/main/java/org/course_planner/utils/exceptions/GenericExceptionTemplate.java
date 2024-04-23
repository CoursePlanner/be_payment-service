package org.course_planner.utils.exceptions;

import org.springframework.http.HttpStatus;

public interface GenericExceptionTemplate {
    HttpStatus getHttpStatus();

    void setHttpStatus(HttpStatus httpStatus);

    String getMessage();

    void setMessage(String message);

    Throwable getCause();

    void setCause(Throwable cause);

    String getInstanceName();
}
