package dev._2lstudios.elasticbungee.broker;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.api.broker.Message;
import dev._2lstudios.elasticbungee.api.broker.MessageBroker;
import dev._2lstudios.elasticbungee.api.broker.Subscription;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisMessageBroker implements MessageBroker {

    private final Jedis provider; // Redis client used to receive packets.
    private final Jedis dispatcher; // Redis client used to send packets.

    public RedisMessageBroker(final String host, final int port, final String password) {
        this.provider = new Jedis(host, port);
        this.dispatcher = new Jedis(host, port);

        if (password != null) {
            this.provider.auth(password);
            this.dispatcher.auth(password);
        }

        this.provider.ping();
    }

    @Override
    public void publish(final String channel, final String content) {
        final String server = ElasticBungee.getInstance().getServerID();
        this.dispatcher.publish(channel, server + "!!" + content);
    }

    @Override
    public void subscribe(final String channel, final Subscription subscription) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                provider.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String rawMessage) {
                        subscription.onReceive(Message.fromString(rawMessage));
                    }
                }, channel);
            }
        }).start();
    }
}
