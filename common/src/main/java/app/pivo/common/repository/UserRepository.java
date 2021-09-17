package app.pivo.common.repository;

import app.pivo.common.entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public Optional<User> findByIdOptional(UUID _id) {
        return find("_id", _id).firstResultOptional();
    }

}
