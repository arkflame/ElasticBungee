package dev._2lstudios.elasticbungee.sync;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.redis.RedisMessage;
import dev._2lstudios.elasticbungee.redis.RedisSubscription;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendAllSync implements RedisSubscription {
    public final static String CHANNEL = "send_all";

    private final ElasticBungee plugin;

    public SendAllSync(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void sendAllServer(final String player, final String server) {
        this.plugin.getMessageBroker().publish(CHANNEL, player + ":" + server);
    }

    @Override
    public void onReceive(final RedisMessage message) {
        if (!message.getChannel().equals(CHANNEL)) {
            return;
        }

        final String[] parts = message.getContent().split(":", 2);

        final String sourceName = parts[0];
        final String targetName = parts[1];

        final ServerInfo source = this.plugin.getProxy().getServerInfo(sourceName);
        final ServerInfo target = this.plugin.getProxy().getServerInfo(targetName);

        if (source != null && target != null) {
            for (final ProxiedPlayer player : source.getPlayers()) {
                player.connect(target);
            }
        }
    }
}
