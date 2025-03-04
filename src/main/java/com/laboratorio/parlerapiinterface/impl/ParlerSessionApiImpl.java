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
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 30/09/2024
 * @updated 04/03/2025
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
    public ParlerSession createSession() {
        return this.authenticateUser();
    }
    
    private String getCookieWithXsrfToken(Map<String, List<String>> headers) {
        if (headers.containsKey("Set-Cookie")) {
            List<String> cookies = headers.get("Set-Cookie");
            for (String cookie : cookies) {
                if (cookie.contains("XSRF-TOKEN")) {
                    return cookie;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public String sendMagicLink() {
        String endpoint = this.apiConfig.getProperty("sendMagicLink_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("sendMagicLink_ok_status"));
        String correctResponse = this.apiConfig.getProperty("sendMagicLink_response");
        
        try {
            ParlerMagicLinkRequest magicLinkRequest = new ParlerMagicLinkRequest(this.userEmail);
            String requestJson = this.gson.toJson(magicLinkRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Accept", "*/*");
            request.addApiHeader("Accept-Encoding", "gzip, deflate, br");
            request.addApiHeader("User-Agent", "PostmanRuntime/7.43.0");
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Respuesta magic link: " + response.getResponseStr());
            ParlerMagicLinkResponse magicLinkResponse = this.gson.fromJson(response.getResponseStr(), ParlerMagicLinkResponse.class);
            if (!magicLinkResponse.getMessage().equals(correctResponse)) {
                throw new ParlerApiException(ParlerSessionApiImpl.class.getName(), "Se ha producido enviando el magic link al correo " + this.userEmail);
            }
            
            return getCookieWithXsrfToken(response.getHttpHeaders());
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerMagicLinkResponse.class.getName(), e.getMessage());
        }
    }

    @Override
    public ParlerSession authenticateUser(int code, String cookieStr) {
        String endpoint = this.apiConfig.getProperty("authenticateUser_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("authenticateUser_ok_status"));
        
        try {
            ParlerAuthenticateRequest loginRequest = new ParlerAuthenticateRequest(this.userEmail, code);
            String requestJson = this.gson.toJson(loginRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Accept", "*/*");
            request.addApiHeader("Accept-Encoding", "gzip, deflate, br");
            request.addApiHeader("User-Agent", "PostmanRuntime/7.43.0");
            request.addApiHeader("Connection", "keep-alive");
            if (cookieStr != null) {
                request.addApiHeader("Cookie", cookieStr);
            }
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Respuesta de authenticateUser: " + response.getResponseStr());
            
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
            String cookieStr = this.sendMagicLink();
            
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
            
            return this.authenticateUser(code, cookieStr);
        } catch (Exception e) {
            throw new ParlerApiException(ParlerMagicLinkResponse.class.getName(), e.getMessage());
        }
    }
    
    private int extractParlerCode(String content) {
        int code = -1;
        
        int pos = content.indexOf(": ");
        if (pos != -1) {
            String codeStr = content.substring(pos + 2, pos + 8);
            log.debug("Código de autenticación: " + codeStr);
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