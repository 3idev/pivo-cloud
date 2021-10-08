package app.pivo.common.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorCode {

    // Common
    VALIDATION_ERROR(400, "api.common.validation_error", "please send it correctly"),
    NOT_AUTHORIZED(401, "api.common.not_authorized", "please sign in"),
    INVALID_TOKEN(401, "api.common.invalid_token", "jwt token is invalid"),
    FORBIDDEN(403, "api.common.forbidden", "forbidden"),
    PAGE_NOT_FOUND(404, "api.common.page_not_found", "page not found"),
    METHOD_NOT_ALLOWED(405, "api.common.method_not_allowed", "method not allowed"),
    NOT_ACCEPTABLE(406, "api.common.not_acceptable", "not acceptable"),
    UNKNOWN(500, "api.common.unknown", "unknown"),

    // User
    INVALID_CREDENTIALS(400, "api.user.invalid_credentials", "invalid credentials"),
    USER_NOT_FOUND(404, "api.user.not_found", "unable to find user"),
    USER_ALREADY_EXISTS(409, "api.user.already_exists", "user already exists"),

    // Cloud
    OBJECT_NOT_FOUND(404, "api.cloud.object_not_found", "Unable to find that object"),
    CANNOT_SHARE_ARCHIVED_FILE(400, "api.cloud.cannot_share_archived_file", "Unable to share archived file"),
    NOT_YOUR_RESOURCE(403, "api.cloud.not_your_resource", "This is not your resource.");

    private final int status;
    private final String code;
    private final String msg;

    @Override
    public String toString() {
        return this.code;
    }

}
