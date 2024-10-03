package com.laboratorio.parlerapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/10/2024
 * @updated 01/10/2024
 */
@Getter @Setter @AllArgsConstructor
public class ParlerRegisterUploadRequest {
    private String bucket;
    private String content_type;
    private String expires;
    private String visibility;

    public ParlerRegisterUploadRequest(String content_type) {
        this.bucket = "";
        this.content_type = content_type;
        this.expires = "";
        this.visibility = "public-read";
    }
}