package com.laboratorio.parlerapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 30/09/2024
 * @updated 04/02/2025
 */

@Getter @Setter @AllArgsConstructor
public class ParlerMagicLinkRequest {
    private String email;
}