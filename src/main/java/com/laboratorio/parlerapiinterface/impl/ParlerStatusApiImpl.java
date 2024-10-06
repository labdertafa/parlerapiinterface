package com.laboratorio.parlerapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.exceptions.UtilsApiException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.ImageMetadata;
import com.laboratorio.clientapilibrary.utils.PostUtils;
import com.laboratorio.parlerapiinterface.ParlerStatusApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import com.laboratorio.parlerapiinterface.model.request.ParlerGetImageUrlRequest;
import com.laboratorio.parlerapiinterface.model.request.ParlerRegisterUploadRequest;
import com.laboratorio.parlerapiinterface.model.request.ParlerStatusRequest;
import com.laboratorio.parlerapiinterface.model.response.ParlerActionResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerImageUploadResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerPostResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerRegisterUploadResponse;
import java.io.File;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/10/2024
 * @updated 06/10/2024
 */
public class ParlerStatusApiImpl extends ParlerBaseApi implements ParlerStatusApi {
    public ParlerStatusApiImpl(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    @Override
    public ParlerStatus postStatus(String text) {
        return this.postStatusWithImage(text, null);
    }

    @Override
    public ParlerStatus postStatus(String text, String imagePath) {
        try {
            ParlerImageUploadResponse image = this.uploadImage(imagePath);
            return postStatusWithImage(text, image);
        } catch (Exception e) {
            throw  e;
        }
    }

    @Override
    public ParlerStatus postStatusWithImage(String text, ParlerImageUploadResponse image) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));
        
        try {
            ParlerStatusRequest statusRequest;
            if (image == null) {
                statusRequest = new ParlerStatusRequest(text);
            } else {
                statusRequest = new ParlerStatusRequest(text, image.getUrl());
            }
            String requestJson = this.gson.toJson(statusRequest);
            log.debug("Status request: " + requestJson);
            
            String uri = endpoint;
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.PUT, requestJson);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request.addApiHeader("Connection", "keep-alive");
            
            ApiResponse response = this.client.executeApiRequest(request);
            ParlerPostResponse postResponse = this.gson.fromJson(response.getResponseStr(), ParlerPostResponse.class);
            
            return postResponse.getData();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    @Override
    public ParlerImageUploadResponse uploadImage(String filePath) {
        String endpoint1 = this.apiConfig.getProperty("registerUpload_endpoint");
        int okStatus1 = Integer.parseInt(this.apiConfig.getProperty("registerUpload_ok_status"));
        int okStatus2 = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));
        String endpoint3 = this.apiConfig.getProperty("getUploadUrl_endpoint");
        int okStatus3 = Integer.parseInt(this.apiConfig.getProperty("getUploadUrl_ok_status"));
        
        try {
            // Se registra la subida de la imagen
            File file = new File(filePath);
            ImageMetadata imageMetadata = PostUtils.extractImageMetadata(filePath);
            ParlerRegisterUploadRequest uploadRequest = new ParlerRegisterUploadRequest(imageMetadata.getMimeType());
            String request1Json = this.gson.toJson(uploadRequest);
            
            String uri1 = endpoint1;
            
            ApiRequest request1 = new ApiRequest(uri1, okStatus1, ApiMethodType.POST, request1Json);
            request1.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response1 = this.client.executeApiRequest(request1);
            ParlerRegisterUploadResponse uploadResponse = this.gson.fromJson(response1.getResponseStr(), ParlerRegisterUploadResponse.class);
            
            log.debug("Registro imagen: " + response1.getResponseStr());
            
            // Se sube la imagen al servidor
            ApiRequest request2 = new ApiRequest(uploadResponse.getUrl(), okStatus2, ApiMethodType.PUT, file);
            request2.addApiHeader("X-Amz-Acl", uploadResponse.getHeaders().getXAmzAcl().get(0));
            
            this.client.executeApiRequest(request2);
            
            log.debug("Se ha cargado la imagen correctamente al servidor!");
            
            // Se recuperar la URL de la imagen en el servidor
            ParlerGetImageUrlRequest urlRequest = new ParlerGetImageUrlRequest(uploadResponse.getUuid(), uploadResponse.getKey(),
                    uploadResponse.getBucket(), file.getName(), imageMetadata.getMimeType());
            String request3Json = this.gson.toJson(urlRequest);
            
            log.debug("Payload para recuperar la URL de la imagen: " + request3Json);
            
            String uri3 = endpoint3;
            
            ApiRequest request3 = new ApiRequest(uri3, okStatus3, ApiMethodType.POST, request3Json);
            request3.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request3.addApiHeader("Connection", "keep-alive");
            
            ApiResponse response3 = this.client.executeApiRequest(request3);
            
            log.debug("Url Obtenida: " + response3.getResponseStr());
            
            return this.gson.fromJson(response3.getResponseStr(), ParlerImageUploadResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (UtilsApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), e.getMessage());
        }
    }   

    @Override
    public boolean deleteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("deleteStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("deleteStatus_ok_status"));
        
        try {
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.DELETE);
            request.addApiPathParam("ulid", id);
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            ApiResponse response = this.client.executeApiRequest(request);
            ParlerActionResponse actionResponse = this.gson.fromJson(response.getResponseStr(), ParlerActionResponse.class);
            
            return actionResponse.isSuccess();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), e.getMessage());
        }
    }
}