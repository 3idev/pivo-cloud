package app.pivo.common.filter.security;

import app.pivo.common.entity.User;
import lombok.*;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserContext implements SecurityContext {

    private User user;

    @Override
    public Principal getUserPrincipal() {
        return this.user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
