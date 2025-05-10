package com.laboratorio.parlerapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.clientapilibrary.ApiClient;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerAccount;
import com.laboratorio.parlerapiinterface.model.ParlerAccountList;
import com.laboratorio.parlerapiinterface.model.request.ParlerGetAccountsByIdRequest;
import com.laboratorio.parlerapiinterface.model.response.ParlerAccountListResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerGetAccountsByIdResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 30/09/2024
 * @updated 10/05/2025
 */
public class ParlerBaseApi {
    protected static final Logger log = LogManager.getLogger(ParlerBaseApi.class);
    protected final ApiClient client;
    protected final ReaderConfig apiConfig;
    protected final Gson gson;
    protected String accessToken = "";

    public ParlerBaseApi() {
        this.apiConfig = new ReaderConfig("config//parler_api.properties");
        this.client = new ApiClient();
        this.gson = new Gson();
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    protected void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getCause().getMessage());
        }
    }
    
    protected void addHeaders(ApiRequest request, String token) {
        request.addApiHeader("Content-Type", "application/json");
        request.addApiHeader("Authorization", "Bearer " + token);
        request.addApiHeader("Accept", "*/*");
        request.addApiHeader("Accept-Encoding", "gzip, deflate, br");
        request.addApiHeader("User-Agent", "PostmanRuntime/7.43.0");
    }
    
    protected List<ParlerAccount> getAccountsDetailsById(List<String> usersId) {
        String endpoint = this.apiConfig.getProperty("getAccountById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountById_ok_status"));
        
        try {
            ParlerGetAccountsByIdRequest idsRequest = new ParlerGetAccountsByIdRequest(usersId);
            String requestJson = this.gson.toJson(idsRequest);
                    
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            this.addHeaders(request, this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            List<ParlerGetAccountsByIdResponse> accountsByIdResponse = this.gson.fromJson(response.getResponseStr(), new TypeToken<List<ParlerGetAccountsByIdResponse>>(){}.getType());
            if (accountsByIdResponse.isEmpty()) {
                throw new ParlerApiException(ParlerAccountApiImpl.class.getName(), "Se ha recibido una respuesta vacía consultando usuarios por Id");
            }
            
            return accountsByIdResponse.get(0).getData();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerAccountApiImpl.class.getName(), e.getMessage());
        }
    }
    
    // Función que devuelve una página de seguidores o seguidos de una cuenta
    private ParlerAccountListResponse getAccountPage(String uri, int okStatus, String posicionInicial) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            this.addHeaders(request, this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), ParlerAccountListResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerBaseApi.class.getName(), e.getMessage());
        }
    }
    
    protected ParlerAccountList getAccountList(String uri, int okStatus, int quantity, String posicionInicial) {
        List<ParlerAccount> accounts = null;
        boolean continuar = true;
        String cursor = posicionInicial;
        
        try {
            do {
                ParlerAccountListResponse accountListResponse = this.getAccountPage(uri, okStatus, cursor);
                
                // Se obtienen los detalles de los usuarios
                List<String> ids = accountListResponse.getData().stream()
                                .map(user -> user.getUlid())
                                .collect(Collectors.toList());
                List<ParlerAccount> usersDetails;
                if (!ids.isEmpty()) {
                    usersDetails = this.getAccountsDetailsById(ids);
                } else {
                    usersDetails = new ArrayList<>();
                }
                
                if (accounts == null) {
                    accounts = usersDetails;
                } else {
                    accounts.addAll(usersDetails);
                }
                
                cursor = accountListResponse.getNext_cursor();
                log.debug("Cantidad: " + quantity + ". Recuperados: " + accounts.size() + ". Cursor: " + cursor);
                if (quantity > 0) {
                    if ((accounts.size() >= quantity) || (cursor == null)) {
                        continuar = false;
                    }
                } else {
                    if (cursor == null) {
                        continuar = false;
                    }
                }
            } while (continuar);

            if (quantity == 0) {
                return new ParlerAccountList(accounts, cursor);
            }
            
            return new ParlerAccountList(accounts.subList(0, Math.min(quantity, accounts.size())), cursor);
        } catch (Exception e) {
            throw e;
        }
    }
    
    protected List<String> getAccountIdsList(String uri, int okStatus) {
        List<String> accountIds = null;
        boolean continuar = true;
        String cursor = null;
        
        try {
            do {
                ParlerAccountListResponse accountListResponse = this.getAccountPage(uri, okStatus, cursor);
                
                // Se obtienen los detalles de los usuarios
                List<String> ids = accountListResponse.getData().stream()
                                .map(user -> user.getUlid())
                                .collect(Collectors.toList());
                if (accountIds == null) {
                    accountIds = ids;
                } else {
                    accountIds.addAll(ids);
                }
                
                cursor = accountListResponse.getNext_cursor();
                log.debug("Recuperados: " + accountIds.size() + ". Cursor: " + cursor);
                if (cursor == null) {
                    continuar = false;
                }
            } while (continuar);

            return accountIds;
        } catch (Exception e) {
            throw e;
        }
    }
}