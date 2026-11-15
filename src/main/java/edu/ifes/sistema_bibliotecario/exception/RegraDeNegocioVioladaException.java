package edu.ifes.sistema_bibliotecario.exception;

public class RegraDeNegocioVioladaException extends RuntimeException{
    public RegraDeNegocioVioladaException(String message) {
        super(message);
    }
}
