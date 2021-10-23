package dev._2lstudios.elasticbungee.api.errors;

public class NoSuchStorageException extends Exception {
    public NoSuchStorageException(final String name) {
        super("No such storage-container named " + name);
    }
}
