package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.parlerapiinterface.ParlerNotificationApi;
import com.laboratorio.parlerapiinterface.impl.ParlerNotificationApiImpl;
import com.laboratorio.parlerapiinterface.model.ParlerNotificationList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/10/2024
 * @updated 10/05/2025
 */
public class ParlerNotificationApiTest {
    private ParlerNotificationApi notificationApi;
    
    @BeforeEach
    public void initTest() {
        ReaderConfig config = new ReaderConfig("config//parler_api.properties");
        String accessToken = config.getProperty("test_access_token");
        notificationApi = new ParlerNotificationApiImpl(accessToken);
    }
    
    @Test
    public void getAllNotifications() throws Exception {
        ParlerNotificationList notificationList = notificationApi.getAllNotifications();
        
        assertTrue(notificationList.getNotifications().size() > 5);
        assertTrue(notificationList.getCursor() != null);
    }
    
    @Test
    public void getAllNotificationsWithInitialPosition() throws Exception {
        String posicionInicial = "2024-10-02T01:40:52.000000Z";
        
        ParlerNotificationList notificationList = notificationApi.getAllNotifications(posicionInicial);
        
        assertTrue(notificationList.getNotifications().size() > 0);
        assertTrue(notificationList.getCursor().compareTo(posicionInicial) > 0);
    }
}