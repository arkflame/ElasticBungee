package dev._2lstudios.elasticbungee.commands;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class KickCommand {
    private final ElasticBungee plugin;

    public KickCommand(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(new ComponentBuilder("Usage: /eb <player> <reason>").color(ChatColor.RED).create());
            return;
        }

        String username = null;
        String reason = null;

        for (final String arg : args) {
            if (username == null) {
                username = arg;
            } else if (reason == null) {
                reason = arg;
            } else {
                reason += " " + arg;
            }
        }

        this.plugin.getKickSync().kick(username, reason);
        MessageUtils.sendMessage(sender, "&ePlayer &b(" + username + ") &ekick request has been sent to all nodes.");
    }
}
