package com.laboratorio.parlerapiinterface.model;

import com.laboratorio.parlerapiinterface.utils.ParlerApiConfig;
import java.util.List;
import java.util.Objects;
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
public class ParlerAccount {
    private String ulid;
    private String name;
    private String username;
    private String bio;
    private String website;
    private String website_name;
    private String background;
    private List<String >badges;
    private int followers;
    private int following;
    private int friendCount;
    private int postCount;
    private int videoCount;
    private int burstCount;
    private String emailVerified;
    private String updatedAt;
    private long updatedAtEpoch;
    private ParlerProfileEngagement ProfileEngagement;
    
    public boolean isSeguidorPotencial() {
        if ((this.ProfileEngagement.isBlockedByYou()) || (this.ProfileEngagement.isBanned()) || (this.ProfileEngagement.isFollowingYou()) || (this.ProfileEngagement.isBlocked())) {
            return false;
        }
        
        if (this.following < 2) {
            return false;
        }

        return 2 * this.following >= this.followers;
    }
    
    public boolean isFuenteSeguidores() {
        ParlerApiConfig config = ParlerApiConfig.getInstance();
        int umbral = Integer.parseInt(config.getProperty("umbral_fuente_seguidores"));
        return this.followers >= umbral;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParlerAccount other = (ParlerAccount) obj;
        if (!Objects.equals(this.ulid, other.ulid)) {
            return false;
        }
        return Objects.equals(this.username, other.username);
    }
}