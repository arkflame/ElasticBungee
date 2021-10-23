package dev._2lstudios.elasticbungee.redis;

import java.util.ArrayList;
import java.util.List;

import dev._2lstudios.elasticbungee.ElasticBungee;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisMessageBroker {
    private static final String REDIS_CHANNEL = "ELASTIC_BUNGEE";

    private final Jedis provider; // Redis client used to receive packets.
    private final Jedis dispatcher; // Redis client used to send packets.

    private final List<RedisSubscription> subscriptions;

    public RedisMessageBroker(final String host, final int port, final String password) {
        this.provider = new Jedis(host, port);
        this.dispatcher = new Jedis(host, port);
        this.subscriptions = new ArrayList<>();

        if (password != null && !password.isEmpty()) {
            this.provider.auth(password);
            this.dispatcher.auth(password);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                provider.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String messageChannel, String rawMessage) {
                        if (messageChannel.equalsIgnoreCase(REDIS_CHANNEL)) {
                            for (final RedisSubscription sub : subscriptions) {
                                sub.onReceive(RedisMessage.fromString(rawMessage));
                            }
                        }
                    }
                }, REDIS_CHANNEL);
            }
        }).start();
    }

    public void publish(final String channel, final String content) {
        final String server = ElasticBungee.getInstance().getServerID();
        this.dispatcher.publish(REDIS_CHANNEL, channel + "!!" + server + "!!" + content);
    }

    public void subscribe(final RedisSubscription subscription) {
        this.subscriptions.add(subscription);
    }
}
