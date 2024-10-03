package com.laboratorio.parlerapiinterface.model.response;

import com.laboratorio.parlerapiinterface.model.ParlerAccount;
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
public class ParlerProfileResonse {
    private ParlerAccount data;
}