package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerNotificationList;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/10/2024
 * @updated 02/10/2024
 */
public interface ParlerNotificationApi {
    ParlerNotificationList getAllNotifications() throws Exception;
    ParlerNotificationList getAllNotifications(String posicionInicial) throws Exception;
}