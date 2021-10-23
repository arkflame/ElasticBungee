package dev._2lstudios.elasticbungee.redis;

public interface RedisSubscription {
    public void onReceive(final RedisMessage message);
}
