package com.laboratorio.parlerapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/10/2024
 * @updated 07/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParlerStatusHeader {
    private String ulid;
    private String parentUlid;
    private String userId;
    private String userUpdatedAt;
    private long userUpdatedAtEpoch;
    private String lastActiveAt;

    @Override
    public String toString() {
        return "ParlerStatusHeader{" + "ulid=" + ulid + ", parentUlid=" + parentUlid + ", userId=" + userId + ", userUpdatedAt=" + userUpdatedAt + ", userUpdatedAtEpoch=" + userUpdatedAtEpoch + ", lastActiveAt=" + lastActiveAt + '}';
    }
}