package dev._2lstudios.elasticbungee.broker;

import java.util.ArrayList;
import java.util.List;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.api.broker.Message;
import dev._2lstudios.elasticbungee.api.broker.MessageBroker;
import dev._2lstudios.elasticbungee.api.broker.Subscription;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisMessageBroker implements MessageBroker {

    private static final String REDIS_CHANNEL = "ELASTIC_BUNGEE";

    private final Jedis provider; // Redis client used to receive packets.
    private final Jedis dispatcher; // Redis client used to send packets.

    private final List<Subscription> subscriptions;

    public RedisMessageBroker(final String host, final int port, final String password) {
        this.provider = new Jedis(host, port);
        this.dispatcher = new Jedis(host, port);
        this.subscriptions = new ArrayList<>();

        if (password != null) {
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
                            for (final Subscription sub : subscriptions) {
                                sub.onReceive(Message.fromString(rawMessage));
                            }
                        }
                    }
                }, REDIS_CHANNEL);
            }
        }).start();
    }

    @Override
    public void publish(final String channel, final String content) {
        final String server = ElasticBungee.getInstance().getServerID();
        this.dispatcher.publish(REDIS_CHANNEL, channel + "!!" + server + "!!" + content);
    }

    @Override
    public void subscribe(final Subscription subscription) {
        this.subscriptions.add(subscription);
    }
}
