package com.laboratorio.parlerapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 30/09/2024
 * @updated 30/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerProfileEngagement {
    private boolean isFollowing;
    private boolean isFollowingYou;
    private boolean isSubscribed;
    private boolean isMuted;
    private boolean isBlocked;
    private boolean isBlockedByYou;
    private boolean isBlockedByThem;
    private boolean isBanned;
}