package com.laboratorio.parlerapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.MailChecker;
import com.laboratorio.parlerapiinterface.ParlerSessionApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerSession;
import com.laboratorio.parlerapiinterface.model.request.ParlerAuthenticateRequest;
import com.laboratorio.parlerapiinterface.model.request.ParlerMagicLinkRequest;
import com.laboratorio.parlerapiinterface.model.response.ParlerMagicLinkResponse;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 30/09/2024
 * @updated 06/10/2024
 */
public class ParlerSessionApiImpl extends ParlerBaseApi implements ParlerSessionApi {
    private final String userEmail;
    private final String password;

    public ParlerSessionApiImpl(String userEmail, String password) {
        super();
        this.userEmail = userEmail;
        this.password = password;
    }
    
    @Override
    public ParlerMagicLinkResponse sendMagicLink() {
        String endpoint = this.apiConfig.getProperty("sendMagicLink_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("sendMagicLink_ok_status"));
        String correctResponse = this.apiConfig.getProperty("sendMagicLink_response");
        
        try {
            ParlerMagicLinkRequest magicLinkRequest = new ParlerMagicLinkRequest(this.userEmail);
            String requestJson = this.gson.toJson(magicLinkRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Respuesta magic link: " + response.getResponseStr());
            ParlerMagicLinkResponse magicLinkResponse = this.gson.fromJson(response.getResponseStr(), ParlerMagicLinkResponse.class);
            if (!magicLinkResponse.getMessage().equals(correctResponse)) {
                throw new ParlerApiException(ParlerSessionApiImpl.class.getName(), "Se ha producido enviando el magic link al correo " + this.userEmail);
            }
            
            return magicLinkResponse;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerMagicLinkResponse.class.getName(), e.getMessage());
        }
    }

    @Override
    public ParlerSession authenticateUser(int code) {
        String endpoint = this.apiConfig.getProperty("authenticateUser_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("authenticateUser_ok_status"));
        
        try {
            ParlerAuthenticateRequest loginRequest = new ParlerAuthenticateRequest(this.userEmail, code);
            String requestJson = this.gson.toJson(loginRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.info("Respuesta de authenticateUser: " + response.getResponseStr());
            
            ParlerSession newSession =  this.gson.fromJson(response.getResponseStr(), ParlerSession.class);
            this.setAccessToken(newSession.getAccess_token());
            
            return newSession;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerMagicLinkResponse.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public ParlerSession authenticateUser() {
        try {
            this.sendMagicLink();
            
            // Esperar la llegada del correo
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                log.warn("No se pudo completar el tiempo de espera del email: " + e.getMessage());
            }
            
            int code = this.checkParlerMagicLinkEmail();
            if (code == -1) {
                throw new ParlerApiException(ParlerSessionApiImpl.class.getName(), "No se pudo recuperar el código del correo " + this.userEmail);
            }
            
            return this.authenticateUser(code);
        } catch (Exception e) {
            throw new ParlerApiException(ParlerMagicLinkResponse.class.getName(), e.getMessage());
        }
    }
    
    private int extractParlerCode(String content) {
        int code = -1;
        
        int pos = content.indexOf(": ");
        if (pos != -1) {
            String codeStr = content.substring(pos + 2, pos + 8);
            log.info("Código de autenticación: " + codeStr);
            code = Integer.parseInt(codeStr);
        }
        
        return code;
    }
    
    @Override
    public int checkParlerMagicLinkEmail() {
        String titulo = this.apiConfig.getProperty("parler_email_title");
        String contenido = MailChecker.getFirtMailByTitle(this.userEmail, this.password, titulo);
        if (contenido == null) {
            return -1;
        }
        
        return extractParlerCode(contenido);
    }
}