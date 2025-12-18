package com.laboratorio.parlerapiinterface.exception;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 10/07/2024
 * @updated 18/12/2025
 */
public class ParlerApiException extends RuntimeException {
    private final Throwable causaOriginal;
    
    public ParlerApiException(String message) {
        super(message);
        this.causaOriginal = null;
    }
    
    public ParlerApiException(String message, Throwable causaOriginal) {
        super(message);
        this.causaOriginal = causaOriginal;
    }
    
    @Override
    public String getMessage() {
        if (this.causaOriginal != null) {
            return super.getMessage() + " | Causa original: " + this.causaOriginal.getMessage();
        }
        
        return super.getMessage();
    }
}