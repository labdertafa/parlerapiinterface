package com.laboratorio.api;

import com.laboratorio.parlerapiinterface.ParlerAccountApi;
import com.laboratorio.parlerapiinterface.impl.ParlerAccountApiImpl;
import com.laboratorio.parlerapiinterface.model.ParlerAccount;
import com.laboratorio.parlerapiinterface.model.ParlerAccountList;
import com.laboratorio.parlerapiinterface.utils.ParlerApiConfig;
import java.util.List;
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
 * @created 30/09/2024
 * @updated 22/10/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParlerAccountApiTest {
    private static ParlerAccountApi accountApi;
    
    @BeforeEach
    public void initTest() {
        String accessToken = ParlerApiConfig.getInstance().getProperty("test_access_token");
        accountApi = new ParlerAccountApiImpl(accessToken);
    }
    
    @Test
    public void getAccountByUsername() {
        String username = "Echoquotes";
        
        ParlerAccount account = accountApi.getAccountByUsername(username);
        
        assertEquals(username, account.getUsername());
    }
    
    @Test
    public void getAccountsById() {
        List<String> usersId = List.of("01cwpx25v8annd6ca6xv3kma8j", "01cnpcvhxgjn4nvcgrpjd0zbdp", "01cyjew1pgh1mhfekqymj3ge6e");
        
        List<ParlerAccount> accounts = accountApi.getAccountsById(usersId);
        
        assertEquals(3, accounts.size());
    }
    
    @Test
    public void get30Followers() throws Exception {
        String username = "Echoquotes";
        int cantidad = 30;
        
        ParlerAccountList accountList  = accountApi.getFollowers(username, cantidad);

        assertEquals(cantidad, accountList.getAccounts().size());
        assertTrue(!accountList.getCursor().isEmpty());
    }
    
    @Test
    public void get110Followers() throws Exception {
        String username = "Echoquotes";
        int cantidad = 110;
        
        ParlerAccountList accountList  = accountApi.getFollowers(username, cantidad);

        assertEquals(cantidad, accountList.getAccounts().size());
        assertTrue(!accountList.getCursor().isEmpty());
    }
    
    @Test
    public void getAllFollowers() throws Exception {
        String username = "orangutito";
        
        ParlerAccountList accountList  = accountApi.getFollowers(username);

        assertTrue(!accountList.getAccounts().isEmpty());
        assertTrue(accountList.getCursor() == null);
    }
    
    @Test
    public void getFollowersIds() throws Exception {
        String username = "orangutito";
        
        List<String> list  = accountApi.getFollowersIds(username);

        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void get30Followings() throws Exception {
        String username = "Echoquotes";
        int cantidad = 30;
        
        ParlerAccountList accountList  = accountApi.getFollowings(username, cantidad);

        assertEquals(cantidad, accountList.getAccounts().size());
        assertTrue(!accountList.getCursor().isEmpty());
    }
    
    @Test
    public void get110Followings() throws Exception {
        String username = "Echoquotes";
        int cantidad = 110;
        
        ParlerAccountList accountList  = accountApi.getFollowings(username, cantidad);

        assertEquals(cantidad, accountList.getAccounts().size());
        assertTrue(!accountList.getCursor().isEmpty());
    }
    
    @Test
    public void getAllFollowings() throws Exception {
        String username = "orangutito";
        
        ParlerAccountList accountList  = accountApi.getFollowings(username);

        assertTrue(!accountList.getAccounts().isEmpty());
        assertTrue(accountList.getCursor() == null);
    }
    
    @Test
    public void getFollowingsIds() throws Exception {
        String username = "orangutito";
        
        List<String> list  = accountApi.getFollowingsIds(username);

        assertTrue(!list.isEmpty());
    }
    
    @Test @Order(1)
    public void followAccount() {
        String username = "alina";
        
        boolean result = accountApi.followAccount(username);
        
        assertTrue(result);
    }
    
    @Test @Order(2)
    public void unfollowAccount() {
        String username = "alina";
        
        boolean result = accountApi.unfollowAccount(username);
        
        assertTrue(result);
    }
}