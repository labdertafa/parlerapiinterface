package com.laboratorio.parlerapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 30/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class ParlerAuthenticateRequest {
    private String identifier;
    private int authCode;
}