package com.laboratorio.parlerapiinterface.model.response;

import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/10/2024
 * @updated 07/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerStatusDetailsResponse {
    private List<ParlerStatus> data;
}