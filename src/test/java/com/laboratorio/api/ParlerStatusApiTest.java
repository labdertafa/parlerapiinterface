package com.laboratorio.api;

import com.laboratorio.parlerapiinterface.ParlerStatusApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.impl.ParlerStatusApiImpl;
import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import com.laboratorio.parlerapiinterface.utils.ParlerApiConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/10/2024
 * @updated 03/10/2024
 */
public class ParlerStatusApiTest {
    private static ParlerStatusApi statusApi;
    
    @BeforeEach
    public void initTest() {
        String accessToken = ParlerApiConfig.getInstance().getProperty("test_access_token");
        statusApi = new ParlerStatusApiImpl(accessToken);
    }
    
    @Test
    public void postStatus() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático";
        
        ParlerStatus status = statusApi.postStatus(text);
        
        assertTrue(!status.getId().isBlank());
    }
    
    @Test
    public void postInvalidStatus() {
        statusApi = new ParlerStatusApiImpl("INVALID_TOKEN");
        
        assertThrows(ParlerApiException.class, () -> {
            statusApi.postStatus("");
        });
    }
    
    @Test
    public void postImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático #SiguemeYTeSigo";
        
        ParlerStatus status = statusApi.postStatus(text, imagen);
        assertTrue(!status.getId().isEmpty());
        assertEquals(text, status.getBody());
    }
}