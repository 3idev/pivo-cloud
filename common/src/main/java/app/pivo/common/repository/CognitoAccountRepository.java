package app.pivo.common.repository;

import app.pivo.common.entity.CognitoAccount;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CognitoAccountRepository implements PanacheMongoRepository<CognitoAccount> {

    public Optional<CognitoAccount> findByUserIdOptional(String _id) {
        return find("userId", _id).firstResultOptional();
    }

}
