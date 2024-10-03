package com.laboratorio.parlerapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.exceptions.UtilsApiException;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.utils.ImageMetadata;
import com.laboratorio.clientapilibrary.utils.PostUtils;
import com.laboratorio.parlerapiinterface.ParlerStatusApi;
import com.laboratorio.parlerapiinterface.exception.ParlerApiException;
import com.laboratorio.parlerapiinterface.model.ParlerStatus;
import com.laboratorio.parlerapiinterface.model.request.ParlerGetImageUrlRequest;
import com.laboratorio.parlerapiinterface.model.request.ParlerRegisterUploadRequest;
import com.laboratorio.parlerapiinterface.model.request.ParlerStatusRequest;
import com.laboratorio.parlerapiinterface.model.response.ParlerImageUploadResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerPostResponse;
import com.laboratorio.parlerapiinterface.model.response.ParlerRegisterUploadResponse;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/10/2024
 * @updated 03/10/2024
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
            
            ApiRequest request = new ApiRequest(uri, okStatus, requestJson);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request.addApiHeader("Connection", "keep-alive");
            
            String jsonStr = this.client.executePutRequest(request);
            ParlerPostResponse response = this.gson.fromJson(jsonStr, ParlerPostResponse.class);
            
            return response.getData();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), e.getMessage());
        }
    }
    
    private boolean sendBinaryPost(String targetURL, int okStatus, File binaryFile, String contentType, String xAmzAcl) {
        HttpURLConnection connection = null;
        FileInputStream fileInputStream = null;
        DataOutputStream outputStream = null;

        try {
            // Abrimos la conexión con la URL de destino
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            
            // Configuración de la solicitud PUT
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", contentType); // Tipo de contenido binario
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("X-Amz-Acl", xAmzAcl);

            // Leemos el archivo binario
            fileInputStream = new FileInputStream(binaryFile);

            // Creamos el stream de salida para enviar los datos binarios
            outputStream = new DataOutputStream(connection.getOutputStream());

            // Buffer para leer y enviar los bytes
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Escribimos el contenido del archivo en el stream de salida
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush(); // Asegurarse de que se envíen todos los datos

            // Obtener la respuesta del servidor
            int responseCode = connection.getResponseCode();
            log.debug("Código de respuesta subida fichero: " + responseCode);
            if (responseCode == okStatus) {
                log.debug("Archivo enviado con éxito.");
                return true;
            } else {
                log.error("Error al enviar archivo. Código de error: " + responseCode);
                throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), "No se ha podido subir un fichero. Código de error: " + responseCode);
            }

        } catch (IOException e) {
            log.error("Error subiendo fichero: " + e.getMessage());
            throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), "No se ha podido subir un fichero");
        } finally {
            // Cerrar todos los recursos
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                log.warn("Error liberando los recursos: " + e.getMessage());
            }
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
            
            ApiRequest request1 = new ApiRequest(uri1, okStatus1, request1Json);
            request1.addApiHeader("Content-Type", "application/json");
            request1.addApiHeader("Authorization", "Bearer " + this.accessToken);
            
            String jsonStr = this.client.executePostRequest(request1);
            ParlerRegisterUploadResponse uploadResponse = this.gson.fromJson(jsonStr, ParlerRegisterUploadResponse.class);
            
            log.debug("Registro imagen: " + jsonStr);
            
            // Se sube la imagen al servidor
            if (!this.sendBinaryPost(uploadResponse.getUrl(), okStatus2, file, imageMetadata.getMimeType(), uploadResponse.getHeaders().getXAmzAcl().get(0))) {
                throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), "Error subiendo la imagen");
            }
            
            log.debug("Se ha cargado la imagen correctamente al servidor!");
            
            // Se recuperar la URL de la imagen en el servidor
            ParlerGetImageUrlRequest urlRequest = new ParlerGetImageUrlRequest(uploadResponse.getUuid(), uploadResponse.getKey(),
                    uploadResponse.getBucket(), file.getName(), imageMetadata.getMimeType());
            String request3Json = this.gson.toJson(urlRequest);
            
            log.debug("Recuperando la Url: " + request3Json);
            
            String uri3 = endpoint3;
            
            ApiRequest request3 = new ApiRequest(uri3, okStatus3, request3Json);
            request3.addApiHeader("Content-Type", "application/json");
            request3.addApiHeader("Authorization", "Bearer " + this.accessToken);
            request3.addApiHeader("Connection", "keep-alive");
            
            jsonStr = this.client.executePostRequest(request3);
            
            log.debug("Url Obtenida: " + jsonStr);
            
            return this.gson.fromJson(jsonStr, ParlerImageUploadResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (UtilsApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ParlerApiException(ParlerStatusApiImpl.class.getName(), e.getMessage());
        }
    }   
}