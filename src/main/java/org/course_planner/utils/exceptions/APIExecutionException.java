package org.course_planner.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class APIExecutionException extends RuntimeException implements GenericExceptionTemplate {
    public static final String instanceName = "org.course_planner.utils.exceptions.APIExecutionException";
    private HttpStatus httpStatus;
    private String message;
    private Throwable cause;

    public APIExecutionException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public APIExecutionException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
