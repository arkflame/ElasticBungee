package dev._2lstudios.elasticbungee.redis;

import redis.clients.jedis.Jedis;

public class RedisStorage {

    private final Jedis client; // Redis client used to store data;

    public RedisStorage(final String host, final int port, final String password) {
        this.client = new Jedis(host, port);

        if (password != null && !password.isEmpty()) {
            this.client.auth(password);
        }

        this.client.ping();
    }

    public String get(String key) {
        return this.client.get(key);
    }

    public void set(String key, String value) {
        this.client.set(key, value);
    }

    public void delete(String key) {
        this.client.del(key);
    }
}
