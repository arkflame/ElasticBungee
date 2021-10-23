package dev._2lstudios.elasticbungee.storage;

import dev._2lstudios.elasticbungee.api.errors.NoSuchStorageException;
import dev._2lstudios.elasticbungee.api.storage.StorageContainer;

public class StorageFactory {
    public static StorageContainer craftStorage(final String driver, final String host, final int port,
            final String password) throws NoSuchStorageException {
        switch (driver) {
        case "redis":
            return new RedisStorage(host, port, password.isEmpty() ? null : password);
        default:
            throw new NoSuchStorageException(driver);
        }
    }
}
