package com.laboratorio.api;

import com.laboratorio.parlerapiinterface.ParlerStatusApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.impl.ParlerStatusApiImpl;
import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import com.laboratorio.parlerapiinterface.model.ParlerStatusHeader;
import com.laboratorio.parlerapiinterface.utils.ParlerApiConfig;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/10/2024
 * @updated 07/10/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParlerStatusApiTest {
    protected static final Logger log = LogManager.getLogger(ParlerStatusApiTest.class);
    private static ParlerStatusApi statusApi;
    private static String postId;
    
    @BeforeEach
    public void initTest() {
        String accessToken = ParlerApiConfig.getInstance().getProperty("test_access_token");
        statusApi = new ParlerStatusApiImpl(accessToken);
    }
    
    @Test @Order(1)
    public void postStatus() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático";
        
        ParlerStatus status = statusApi.postStatus(text);
        postId = status.getId();
        
        assertTrue(!status.getId().isBlank());
    }
    
    @Test
    public void postInvalidStatus() {
        statusApi = new ParlerStatusApiImpl("INVALID_TOKEN");
        
        assertThrows(ParlerApiException.class, () -> {
            statusApi.postStatus("");
        });
    }
    
    @Test @Order(2)
    public void deleteStatus() {
        boolean result =statusApi.deleteStatus(postId);
        
        assertTrue(result);
    }
    
    @Test @Order(3)
    public void postImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático #SiguemeYTeSigo";
        
        ParlerStatus status = statusApi.postStatus(text, imagen);
        postId = status.getId();
        
        assertTrue(!status.getId().isEmpty());
        assertEquals(text, status.getBody());
    }
    
    @Test @Order(4)
    public void deleteStatusWithImage() {
        boolean result =statusApi.deleteStatus(postId);
        
        assertTrue(result);
    }
    
    @Test
    public void getGlobalTimeLineHeaders() {
        int quantity = 50;
        
        List<ParlerStatusHeader> headers = statusApi.getGlobalTimeLineHeaders(quantity);
        
        int i = 1;
        for (ParlerStatusHeader header : headers) {
            log.info(i + ") Header: " + header.toString());
            i++;
        }
        
        assertEquals(quantity, headers.size());
    }
    
    @Test
    public void getGlobalTimeline() {
        int quantity = 20;
        
        List<ParlerStatusHeader> headers = statusApi.getGlobalTimeLineHeaders(quantity);
        assertEquals(quantity, headers.size());
        
        List<String> ulids = headers.stream()
                .map(h -> h.getUlid())
                .collect(Collectors.toList());
        
        List<ParlerStatus> statuses = statusApi.getGlobalTimeline(ulids);
        int i = 0;
        for (ParlerStatus status : statuses) {
            assertEquals(ulids.get(i), status.getId());
            i++;
            log.info(i + ") Status: " + status.toString());
        }
        
        assertEquals(quantity, statuses.size());
    }
}