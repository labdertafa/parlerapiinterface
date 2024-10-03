package com.laboratorio.parlerapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.clientapilibrary.ApiClient;
import com.laboratorio.clientapilibrary.impl.ApiClientImpl;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerAccount;
import com.laboratorio.parlerapiinterface.model.ParlerAccountList;
import com.laboratorio.parlerapiinterface.model.request.ParlerGetAccountsByIdRequest;
import com.laboratorio.parlerapiinterface.model.response.ParlerAccountListResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerGetAccountsByIdResponse;
import com.laboratorio.parlerapiinterface.utils.ParlerApiConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 01/10/2024
 */
public class ParlerBaseApi {
    protected static final Logger log = LogManager.getLogger(ParlerBaseApi.class);
    protected final ApiClient client;
    protected final ParlerApiConfig apiConfig;
    protected final Gson gson;
    protected String accessToken = "";

    public ParlerBaseApi() {
        this.apiConfig = ParlerApiConfig.getInstance();
        this.client = new ApiClientImpl();
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
    
    protected List<ParlerAccount> getAccountsDetailsById(List<String> usersId) {
        String endpoint = this.apiConfig.getProperty("getAccountById_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountById_ok_status"));
        
        try {
            ParlerGetAccountsByIdRequest idsRequest = new ParlerGetAccountsByIdRequest(usersId);
            String requestJson = this.gson.toJson(idsRequest);
                    
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, requestJson);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + accessToken);
            
            String jsonStr = this.client.executePostRequest(request);
            
            List<ParlerGetAccountsByIdResponse> accountsByIdResponse = this.gson.fromJson(jsonStr, new TypeToken<List<ParlerGetAccountsByIdResponse>>(){}.getType());
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
            ApiRequest request = new ApiRequest(uri, okStatus);
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + accessToken);
            
            String jsonStr = this.client.executeGetRequest(request);
            
            return this.gson.fromJson(jsonStr, ParlerAccountListResponse.class);
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
}