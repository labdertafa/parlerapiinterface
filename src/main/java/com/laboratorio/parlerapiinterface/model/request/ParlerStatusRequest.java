package com.laboratorio.parlerapiinterface.model.request;

import java.util.ArrayList;
import java.util.List;
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
public class ParlerStatusRequest {
    private String body;
    private List<String> images;
    private String groupName;

    public ParlerStatusRequest(String body) {
        this.body = body;
        this.images = new ArrayList<>();
        this.groupName = "default";
    }

    public ParlerStatusRequest(String body, String imageUrl) {
        this.body = body;
        this.images = new ArrayList<>();
        this.images.add(imageUrl);
        this.groupName = "default";
    }
}