package com.laboratorio.parlerapiinterface.model;

import com.google.gson.annotations.SerializedName;
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
public class ParlerUploadHeader {
    private List<String> Host;
    @SerializedName("x-amz-acl")
    private List<String> xAmzAcl;
    @SerializedName("Content-Type")
    private String ContentType;
}