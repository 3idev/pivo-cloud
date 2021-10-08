package app.pivo.common.service.token;

import app.pivo.common.exception.InvalidJwtTokenException;

public interface TokenService {

    String KEY_SPEC = "RSA";

    boolean validate(String token) throws InvalidJwtTokenException;

    String generateAccessToken();

    String generateRefreshToken();

}
