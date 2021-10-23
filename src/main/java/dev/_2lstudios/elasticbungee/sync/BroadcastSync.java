package dev._2lstudios.elasticbungee.sync;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.api.broker.Message;
import dev._2lstudios.elasticbungee.api.broker.Subscription;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BroadcastSync implements Subscription {
    public final static String CHANNEL = "broadcast";

    private final ElasticBungee plugin;

    public BroadcastSync(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void broadcast(final String message) {
        this.plugin.getMessageBroker().publish(CHANNEL, message);
    }

    @Override
    public void onReceive(final Message message) {
        this.plugin.getProxy().broadcast(
                new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', message.getContent())).create());
    }
}
