package dev._2lstudios.elasticbungee.api.broker;

public interface Subscription {
    public void onReceive(final Message message);
}
