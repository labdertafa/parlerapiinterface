package com.laboratorio.parlerapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 02/10/2024
 * @updated 02/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerNotification {
    private String notificationId;
    private String createdAt;
    private String fromUserId;
    private String notificationType;
    private int notificationTypeId;
    private String reactionName;
    private boolean seen;
    private boolean read;
    private String postId;
    private String parentId;
}