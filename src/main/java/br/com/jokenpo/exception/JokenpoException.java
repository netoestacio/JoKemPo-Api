package br.com.jokenpo.exception;

import br.com.jokenpo.enumeration.EnumException;

public class JokenpoException extends Exception {

    public JokenpoException(EnumException enumException){
        super(enumException.getCode() + " - " + enumException.getMessage());
    }

    public JokenpoException(String errorMessage){
        super(errorMessage);
    }

}
