package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String message){
        super(message);
    }
}
