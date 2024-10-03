package com.laboratorio.parlerapiinterface;

import com.laboratorio.parlerapiinterface.model.ParlerAccount;
import com.laboratorio.parlerapiinterface.model.ParlerAccountList;
import com.laboratorio.parlerapiinterface.model.ParlerProfileEngagement;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 01/10/2024
 */
public interface ParlerAccountApi {
    ParlerAccount getAccountByUsername(String username);
    List<ParlerAccount> getAccountsById(List<String> usersId);
    
    ParlerAccountList getFollowers(String username) throws Exception; 
    ParlerAccountList getFollowers(String username, int quantity) throws Exception;
    ParlerAccountList getFollowers(String username, int quantity, String posicionInicial) throws Exception;
    
    ParlerAccountList getFollowings(String username) throws Exception;
    ParlerAccountList getFollowings(String username, int quantity) throws Exception;
    ParlerAccountList getFollowings(String username, int quantity, String posicionInicial) throws Exception;
    
    boolean followAccount(String  username);
    boolean unfollowAccount(String  username);
    
    ParlerProfileEngagement checkrelationship(String username);
}