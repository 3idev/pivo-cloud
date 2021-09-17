package app.pivo.common.repository;

import app.pivo.common.entity.Token;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TokenRepository implements PanacheMongoRepository<Token> {

    public Optional<Token> findByAccessTokenOptional(String accessToken) {
        return find("accessToken", accessToken).firstResultOptional();
    }

}
