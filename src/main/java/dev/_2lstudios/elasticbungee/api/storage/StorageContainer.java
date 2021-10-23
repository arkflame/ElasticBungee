package dev._2lstudios.elasticbungee.api.storage;

public interface StorageContainer {

    public String get(final String key);

    public void set(final String key, final String value);

    public void delete(final String key);
}
