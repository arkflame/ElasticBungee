package dev._2lstudios.elasticbungee.api.broker;

public class Message {

    private final String source;
    private final String content;

    public Message(final String source, final String content) {
        this.source = source;
        this.content = content;
    }

    public String getSource() {
        return this.source;
    }

    public String getContent() {
        return this.content;
    }

    public static Message fromString(final String raw) {
        final String[] parts = raw.split("!!", 2);
        return new Message(parts[0], parts[1]);
    }
}
