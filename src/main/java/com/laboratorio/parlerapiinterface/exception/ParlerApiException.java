package com.laboratorio.parlerapiinterface.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/07/2024
 * @updated 10/07/2024
 */
public class ParlerApiException extends RuntimeException {
    private static final Logger log = LogManager.getLogger(ParlerApiException.class);
    
    public ParlerApiException(String className, String message) {
        super(message);
        log.error(String.format("Error %s: %s", className, message));
    }
}