package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException exception){
        //Membuat menyimpan error perfield
        //name -> Name wajib diisi
        //email -> Format email salah
        //position -> Position wajib diisi
        Map <String, String> errors = new HashMap<>();

        //Ambil semua filed yang error
        List<FieldError> fieldErrors =exception.getBindingResult().getFieldErrors();

        //Loop semua field, message kemudian kirim ke String
        //FieldError ini tipe datanya , error nama variabel sementara
        for(FieldError error :fieldErrors){
            String fieldName =error.getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        }

        // karena awalan public ErrorResponse maka hasil hasil terakhir ErrorResponse
        ErrorResponse errorResponse =
                new ErrorResponse(
                        400,
                        "Validation error",
                        errors
                );

        return errorResponse;

    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(EmployeeNotFoundException exception){

        return new ErrorResponse(
                404,
                exception.getMessage(),
                null
        );

    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailDuplicate(DuplicateEmailException exception) {

        return new ErrorResponse(
                409,
                exception.getMessage(),
                null
        );

    }


    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUsernameAlready(UsernameAlreadyExistsException exception){

        return new ErrorResponse(
                409,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(InvalidCrendentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCrendetial(InvalidCrendentialException exception){

        return new ErrorResponse(
                401,
                exception.getMessage(),
                null
        );
    }
}
