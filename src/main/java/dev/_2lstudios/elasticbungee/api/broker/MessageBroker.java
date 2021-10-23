package dev._2lstudios.elasticbungee.api.broker;

public interface MessageBroker {
    public void publish(final String channel, final String content);

    public void subscribe(final String channel, final Subscription subscription);
}
