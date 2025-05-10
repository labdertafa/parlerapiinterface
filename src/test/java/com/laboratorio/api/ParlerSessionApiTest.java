package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.parlerapiinterface.ParlerSessionApi;
import com.laboratorio.parlerapiinterface.impl.ParlerSessionApiImpl;
import com.laboratorio.parlerapiinterface.model.ParlerSession;
import com.laboratorio.parlerapiinterface.model.response.ParlerMagicLinkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 30/09/2024
 * @updated 10/05/2025
 */
public class ParlerSessionApiTest {
    private static ParlerSessionApi sessionApi;
    
    @BeforeEach
    public void initTest() {
        ReaderConfig config = new ReaderConfig("config//parler_api.properties");
        String userEmail = config.getProperty("parler_user_email");
        String password = config.getProperty("parler_email_password");
        sessionApi = new ParlerSessionApiImpl(userEmail, password);
    }
    
/*    @Test
    public void authenticateUser() {
        String cookieStr = sessionApi.sendMagicLink();
        
        assertNotNull(cookieStr);
        
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
        }
        
        int code = sessionApi.checkParlerMagicLinkEmail();
        
        assertTrue(code != -1);
        
        ParlerSession session = sessionApi.authenticateUser(code, cookieStr);
        
        assertTrue(session.getAccess_token().length() > 0);
    } */
}