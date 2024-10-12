package com.laboratorio.parlerapiinterface.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/10/2024
 * @updated 07/10/2024
 */

@Getter @Setter @AllArgsConstructor
public class ParlerStatusDetailsRequest {
    private List<String> ulids;
}