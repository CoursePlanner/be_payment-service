package org.course_planner.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationException extends RuntimeException implements GenericExceptionTemplate {
    public static final String instanceName = "org.course_planner.utils.exceptions.AuthenticationException";
    private HttpStatus httpStatus;
    private String message;
    private Throwable cause;

    public AuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public AuthenticationException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
