package com.laboratorio.parlerapiinterface.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/10/2024
 * @updated 01/10/2024
 */
@Getter @Setter @AllArgsConstructor
public class ParlerGetAccountsByIdRequest {
    private List<String> ulids;
}