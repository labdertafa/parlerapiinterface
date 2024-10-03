package com.laboratorio.parlerapiinterface.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/10/2024
 * @updated 02/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerNotificationList {
    private List<ParlerNotification> notifications;
    private String cursor;
}