package dev._2lstudios.elasticbungee.sync;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.redis.RedisMessage;
import dev._2lstudios.elasticbungee.redis.RedisSubscription;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BroadcastSync implements RedisSubscription {
    public final static String CHANNEL = "broadcast";

    private final ElasticBungee plugin;

    public BroadcastSync(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void broadcast(final String message) {
        this.plugin.getMessageBroker().publish(CHANNEL, message);
    }

    @Override
    public void onReceive(final RedisMessage message) {
        if (!message.getChannel().equals(CHANNEL)) {
            return;
        }

        this.plugin.getProxy().broadcast(
                new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', message.getContent())).create());
    }
}
