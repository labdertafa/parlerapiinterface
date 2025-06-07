package com.laboratorio.parlerapiinterface.impl;

import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.parlerapiinterface.ParlerNotificationApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerNotification;
import com.laboratorio.parlerapiinterface.model.ParlerNotificationList;
import com.laboratorio.parlerapiinterface.model.response.ParlerNotificationsResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 02/10/2024
 * @updated 07/06/2025
 */
public class ParlerNotificationApiImpl extends ParlerBaseApi implements ParlerNotificationApi {
    public ParlerNotificationApiImpl(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    @Override
    public ParlerNotificationList getAllNotifications() throws Exception {
        return this.getAllNotifications(null);
    }
    
    // Función que devuelve una página de notificaciones de una cuenta
    private ParlerNotificationsResponse getNotificationPage(String uri, int okStatus, String posicionInicial) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            this.addHeaders(request, this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getNotificationPage: {}", response.getResponseStr());
                        
            return this.gson.fromJson(response.getResponseStr(), ParlerNotificationsResponse.class);
        } catch (Exception e) {
            throw new ParlerApiException("Error recuperando una página de notificaciones de Parler", e);
        }
    }

    @Override
    public ParlerNotificationList getAllNotifications(String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getNotifications_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getNotifications_ok_status"));
        String nuevaPosicionInicial = posicionInicial;
        
        List<ParlerNotification> notifications = null;
        boolean continuar = true;
        String cursor = null;
        
        try {
            String uri = endpoint;
            
            do {
                ParlerNotificationsResponse notificationsResponse = this.getNotificationPage(uri, okStatus, cursor);
                if (notifications == null) {
                    if (!notificationsResponse.getData().isEmpty()) {
                        nuevaPosicionInicial = notificationsResponse.getData().get(0).getCreatedAt();
                    }
                    notifications = notificationsResponse.getData();
                } else {
                    notifications.addAll(notificationsResponse.getData());
                }
                
                cursor = notificationsResponse.getNext_cursor();
                log.debug("getAllNotifications. Recuperados: " + notifications.size() + ". Cursor: " + cursor);
                if (notificationsResponse.getData().isEmpty()) {
                    continuar = false;
                } else {
                    if (cursor == null) {
                        continuar = false;
                    }
                    
                    if (posicionInicial != null) {
                        List<ParlerNotification> list = notificationsResponse.getData();
                        for (int i = list.size() - 1; i >= 0; i--) {
                            String notiftime = list.get(i).getCreatedAt();
                            if (notiftime.compareTo(posicionInicial) <= 0) {
                                continuar = false;
                                break;
                            }
                        }
                    }
                }
            } while (continuar);
            
            if (posicionInicial == null) {
                return new ParlerNotificationList(notifications, nuevaPosicionInicial);
            }
            
            List<ParlerNotification> filtredNotifications = notifications.stream()
                    .filter(n -> n.getCreatedAt().compareTo(posicionInicial) > 0)
                    .collect(Collectors.toList());
            log.debug("filtredNotificationsList size: " + filtredNotifications.size() + "nueva posición inicial: " + nuevaPosicionInicial);
            return new ParlerNotificationList(filtredNotifications, nuevaPosicionInicial);
        } catch (Exception e) {
            throw e;
        }
    }
}