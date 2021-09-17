package app.pivo.common.repository;

import app.pivo.common.define.RedisPrefix;
import io.quarkus.redis.client.RedisClient;
import io.vertx.redis.client.Response;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class RedisRepository {

    @Inject
    RedisClient redis;

    @Inject
    Logger log;

    // GET
    public Response get(RedisPrefix prefix, String key) {
        return get(prefix.getName(key));
    }

    public Response get(String key) {
        return redis.get(key);
    }

    // SET
    public Response set(RedisPrefix prefix, String key, Object value) throws NoSuchMethodException {
        return set(prefix.getName(key), value);
    }

    public Response set(RedisPrefix prefix, String key, String value) {
        return set(prefix.getName(key), value);
    }

    public Response set(String key, Object value) throws NoSuchMethodException {
        String val;
        try {
            val = value.toString();
            return redis.set(Arrays.asList(key, val));
        } catch (NoSuchMethodError e) {
            log.errorf("%s class doesn't have toString method", value.getClass().getName());
            throw new NoSuchMethodException(e.getMessage());
        }
    }

    public Response set(String key, String value) {
        return redis.set(Arrays.asList(key, value));
    }

    // SET WITH EXPIRE
    public void setWithExpire(RedisPrefix prefix, String key, String value, Long ttl) {
        this.set(prefix, key, value);
        this.expire(prefix, key, ttl);
    }

    public void setWithExpire(String key, String value, Long ttl) {
        this.set(key, value);
        this.expire(key, ttl);
    }

    // DEL
    public Response del(RedisPrefix prefix, String key) {
        return del(prefix.getName(key));
    }

    public Response del(String key) {
        return redis.del(List.of(key));
    }

    // EXISTS
    public boolean exists(RedisPrefix prefix, String key) {
        return exists(prefix.getName(key));
    }

    public boolean exists(String key) {
        Response res = redis.exists(List.of(key));
        if (null != res) {
            try {
                return res.toBoolean();
            } catch (Exception ignore) {
                return false;
            }
        }

        return false;
    }

    // EXPIRE
    public Response expire(RedisPrefix prefix, String key, Long ttl) {
        return this.expire(prefix.getName(key), ttl);
    }

    public Response expire(String key, Long ttl) {
        return expire(key, ttl.toString());
    }

    public Response expire(String key, String ttl) {
        return redis.expire(key, ttl);
    }

    // TTL
    public long ttl(RedisPrefix prefix, String key) {
        return ttl(prefix.getName(key));
    }

    public long ttl(String key) {
        Response res = redis.ttl(key);
        try {
            if (null != res) {
                return res.toLong();
            }
        } catch (Exception ignore) {
        }
        return 0;
    }

}
