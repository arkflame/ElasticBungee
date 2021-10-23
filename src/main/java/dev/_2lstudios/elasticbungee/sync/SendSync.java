package dev._2lstudios.elasticbungee.sync;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.redis.RedisMessage;
import dev._2lstudios.elasticbungee.redis.RedisSubscription;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendSync implements RedisSubscription {
    public final static String CHANNEL = "send";

    private final ElasticBungee plugin;

    public SendSync(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void sendServer(final String player, final String server) {
        this.plugin.getMessageBroker().publish(CHANNEL, player + ":" + server);
    }

    @Override
    public void onReceive(final RedisMessage message) {
        if (!message.getChannel().equals(CHANNEL)) {
            return;
        }

        final String[] parts = message.getContent().split(":", 2);
        final String username = parts[0];
        final String server = parts[1];

        final ProxiedPlayer proxyPlayer = this.plugin.getProxy().getPlayer(username);

        if (proxyPlayer != null) {
            final ServerInfo target = this.plugin.getProxy().getServerInfo(server);
            if (target != null) {
                proxyPlayer.connect(target);
            }
        }
    }
}
