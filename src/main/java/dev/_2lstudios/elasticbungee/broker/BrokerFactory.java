package dev._2lstudios.elasticbungee.broker;

import dev._2lstudios.elasticbungee.api.broker.MessageBroker;
import dev._2lstudios.elasticbungee.api.errors.NoSuchMessageBrokerException;

public class BrokerFactory {
    public static MessageBroker craftBroker(final String driver, final String host, final int port,
            final String password) throws NoSuchMessageBrokerException {
        switch (driver) {
        case "redis":
            return new RedisMessageBroker(host, port, password.isEmpty() ? null : password);
        default:
            throw new NoSuchMessageBrokerException(driver);
        }
    }
}
