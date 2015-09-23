package com.tiaonaer.ws.security.authorization;

import com.tiaonaer.ws.user.dto.SecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author jason.y.chen
 */
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        LOGGER.debug("Evaluation permission: {} for domain object: {}", permission, targetDomainObject);
        LOGGER.debug("Received authentication: {}", authentication);

        boolean hasPermission = false;

        SecurityRole role = SecurityRole.ROLE_ANONYMOUS;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String principalRole = getRole(userDetails.getAuthorities());
            if (principalRole.equals(SecurityRole.ROLE_USER.name())) {
                role = SecurityRole.ROLE_USER;
            } else if ( principalRole.equals(SecurityRole.ROLE_ADMIN.name())) {
                role = SecurityRole.ROLE_ADMIN;
            }
        }
        SecurityRole minrole = SecurityRole.ROLE_ANONYMOUS;
        switch ( (String)targetDomainObject ) {
            case "Jobs":
                switch ( (String)permission ) {
                    case "update":
                        minrole = SecurityRole.ROLE_ADMIN;
                        break;
                    default:
                        minrole = SecurityRole.ROLE_ANONYMOUS;
                        // Note: Some very loose limit done in client side for searching.
                        break;
                }
                break;
            case "Comment":
                minrole = SecurityRole.ROLE_USER;
                break;
            case "Favorite":
                minrole = SecurityRole.ROLE_USER;
                break;
            case "Complain":
                switch ( (String)permission ) {
                    case "update":
                    case "adminlist":
                        minrole = SecurityRole.ROLE_ADMIN;
                        break;
                    default:
                        minrole = SecurityRole.ROLE_USER;
                        break;
                }
                break;
            default:
                LOGGER.debug("Unknown class: {} for target domain Object: {}", targetDomainObject.getClass(), targetDomainObject);
        }

        if ( minrole.ordinal() <= role.ordinal()) hasPermission = true;

        LOGGER.debug("Returning: {}", hasPermission);

        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        LOGGER.debug("Evaluation permission: {} for target type: {}", permission, targetType);
        LOGGER.debug("Received authentication: {}", authentication);

        //Not required here.

        return false;
    }

    /**
     * Returns the role of the logged in user. We assume that each user has only one
     * role.
     * @param authorities
     * @return  The role of the logged in user.
     */
    private String getRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.iterator().next().getAuthority();
    }
}
