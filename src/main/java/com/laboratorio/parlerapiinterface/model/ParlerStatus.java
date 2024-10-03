package com.laboratorio.parlerapiinterface.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 01/10/2024
 * @updated 01/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerStatus {
    private String id;
    private String rootUlid;
    private String parentUlid;
    private String grandparentUlid;
    private boolean isSensitive;
    private boolean isTrolling;
    private String body;
    private String detectedLanguage;
    private String username;
    ParlerStatusUser user;
    private String postType;
    private String title;
    private List<String> images;
    private List<String> videos;
    private boolean isRepost;
    private boolean isRepostWithComment;
    private String embedUrl;
    private String groupName;
    private String groupId;
    private String createdAt;
    private String updatedAt;
    private boolean isDeleted;
    private boolean isHidden;
    private boolean isProcessing;
    private ParlerPostEngagement postEngagement;
}