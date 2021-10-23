package dev._2lstudios.elasticbungee.redis;

public class RedisMessage {

    private final String channel;
    private final String source;
    private final String content;

    public RedisMessage(final String channel, final String source, final String content) {
        this.channel = channel;
        this.source = source;
        this.content = content;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getSource() {
        return this.source;
    }

    public String getContent() {
        return this.content;
    }

    public static RedisMessage fromString(final String raw) {
        final String[] parts = raw.split("!!", 3);
        return new RedisMessage(parts[0], parts[1], parts[2]);
    }
}
