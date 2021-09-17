package app.pivo.common.service.token.impl;

import app.pivo.common.exception.InvalidJwtTokenException;
import app.pivo.common.service.token.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.bouncycastle.util.io.pem.PemReader;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

@ApplicationScoped
public class JWTServiceImpl implements TokenService {

    @Inject
    Logger log;

    private RSAPublicKey publicKey;

    @PostConstruct
    public void __INIT__() {
        try (InputStream is = getClass().getResourceAsStream("/keys/public.der")) {
            byte[] keyBytes = is.readAllBytes();
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(this.KEY_SPEC);
            this.publicKey = (RSAPublicKey) kf.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get the public key");
        }
    }

    @Override
    public boolean validate(String token) throws InvalidJwtTokenException {
        try {
            Algorithm algorithm = Algorithm.RSA256(this.publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
        } catch (Exception e) {
            throw new InvalidJwtTokenException();
        }

        return true;
    }

}
