package dev._2lstudios.elasticbungee.storage;

import dev._2lstudios.elasticbungee.api.storage.StorageContainer;
import redis.clients.jedis.Jedis;

public class RedisStorage implements StorageContainer {

    private final Jedis client; // Redis client used to store data;

    public RedisStorage(final String host, final int port, final String password) {
        this.client = new Jedis(host, port);

        if (password != null) {
            this.client.auth(password);
        }

        this.client.ping();
    }

    @Override
    public String get(String key) {
        return this.client.get(key);
    }

    @Override
    public void set(String key, String value) {
        this.client.set(key, value);
    }

    @Override
    public void delete(String key) {
        this.client.del(key);
    }
}
