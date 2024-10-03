package com.laboratorio.parlerapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 30/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerSession {
    private String token_type;
    private long expires_in;
    private String access_token;
    private String refresh_token;

    @Override
    public String toString() {
        return "ParlerSession{" + "token_type=" + token_type + ", expires_in=" + expires_in + ", access_token=" + access_token + ", refresh_token=" + refresh_token + '}';
    }
}