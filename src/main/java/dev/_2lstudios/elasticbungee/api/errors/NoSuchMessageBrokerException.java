package dev._2lstudios.elasticbungee.api.errors;

public class NoSuchMessageBrokerException extends Exception {
    public NoSuchMessageBrokerException(final String name) {
        super("No such message-broker named " + name);
    }
}
