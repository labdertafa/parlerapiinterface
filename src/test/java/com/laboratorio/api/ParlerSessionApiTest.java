package com.laboratorio.api;

import com.laboratorio.parlerapiinterface.ParlerSessionApi;
import com.laboratorio.parlerapiinterface.impl.ParlerSessionApiImpl;
import com.laboratorio.parlerapiinterface.model.ParlerSession;
import com.laboratorio.parlerapiinterface.model.response.ParlerMagicLinkResponse;
import com.laboratorio.parlerapiinterface.utils.ParlerApiConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 30/09/2024
 */
public class ParlerSessionApiTest {
    private static ParlerSessionApi sessionApi;
    
    @BeforeEach
    public void initTest() {
        ParlerApiConfig config = ParlerApiConfig.getInstance();
        String userEmail = config.getProperty("parler_user_email");
        String password = config.getProperty("parler_email_password");
        sessionApi = new ParlerSessionApiImpl(userEmail, password);
    }
    
/*    @Test
    public void authenticateUser() {
        String correctResponse = ParlerApiConfig.getInstance().getProperty("sendMagicLink_response");
        
        ParlerMagicLinkResponse response = sessionApi.sendMagicLink();
        
        assertEquals(correctResponse,  response.getMessage());
        
        try {
            Thread.sleep(30000);
        } catch (Exception e) {
        }
        
        int code = sessionApi.checkParlerMagicLinkEmail();
        
        assertTrue(code != -1);
        
        ParlerSession session = sessionApi.authenticateUser(code);
        
        assertTrue(session.getAccess_token().length() > 0);
    } */
}