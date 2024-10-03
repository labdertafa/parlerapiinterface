package com.laboratorio.parlerapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.parlerapiinterface.ParlerAccountApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerAccount;
import com.laboratorio.parlerapiinterface.model.ParlerAccountList;
import com.laboratorio.parlerapiinterface.model.ParlerProfileEngagement;
import com.laboratorio.parlerapiinterface.model.response.ParlerActionResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerProfileResonse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 01/10/2024
 */
public class ParlerAccountApiImpl extends ParlerBaseApi implements ParlerAccountApi {
    public ParlerAccountApiImpl(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    @Override
    public ParlerAccount getAccountByUsername(String username) {
        String endpoint = this.apiConfig.getProperty("getAccountByUsername_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountByUsername_ok_status"));
        
        try {
            String uri = endpoint + "/" + username;
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + accessToken);
            
            String jsonStr = this.client.executeGetRequest(request);
            
            ParlerProfileResonse response =  this.gson.fromJson(jsonStr, ParlerProfileResonse.class);
            
            return response.getData();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public List<ParlerAccount> getAccountsById(List<String> usersId) {
        return this.getAccountsDetailsById(usersId);
    }

    @Override
    public ParlerAccountList getFollowers(String username) throws Exception {
        return this.getFollowers(username, 0);
    }

    @Override
    public ParlerAccountList getFollowers(String username, int quantity) throws Exception {
        return this.getFollowers(username, quantity, null);
    }

    @Override
    public ParlerAccountList getFollowers(String username, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowers_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowers_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowers_ok_status"));
        
        String uri = endpoint + "/"  + username + "/" + complementoUrl;
        
        return this.getAccountList(uri, okStatus, quantity, posicionInicial);
    }

    @Override
    public ParlerAccountList getFollowings(String username) throws Exception {
        return this.getFollowings(username, 0);
    }

    @Override
    public ParlerAccountList getFollowings(String username, int quantity) throws Exception {
        return this.getFollowings(username, quantity, null);
    }

    @Override
    public ParlerAccountList getFollowings(String username, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowings_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowings_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowings_ok_status"));
        
        String uri = endpoint + "/"  + username + "/" + complementoUrl;
        
        return this.getAccountList(uri, okStatus, quantity, posicionInicial);
    }

    @Override
    public boolean followAccount(String username) {
        String endpoint = this.apiConfig.getProperty("followAccount_endpoint");
        String complementoUrl = this.apiConfig.getProperty("followAccount_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("followAccount_ok_status"));
        
        try {
            String uri = endpoint + "/" + username + "/" + complementoUrl;
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            String jsonStr = this.client.executePutRequest(request);
            ParlerActionResponse response = this.gson.fromJson(jsonStr, ParlerActionResponse.class);
            
            return response.isSuccess();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public boolean unfollowAccount(String username) {
        String endpoint = this.apiConfig.getProperty("unfollowAccount_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unfollowAccount_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unfollowAccount_ok_status"));
        
        try {
            String uri = endpoint + "/" + username + "/" + complementoUrl;

            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            String jsonStr = this.client.executeDeleteRequest(request);
            ParlerActionResponse response = this.gson.fromJson(jsonStr, ParlerActionResponse.class);
            
            return response.isSuccess();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public ParlerProfileEngagement checkrelationship(String username) {
        return this.getAccountByUsername(username).getProfileEngagement();
    }   
}