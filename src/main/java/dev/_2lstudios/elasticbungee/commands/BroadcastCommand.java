package dev._2lstudios.elasticbungee.commands;

import dev._2lstudios.elasticbungee.ElasticBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BroadcastCommand {
    private final ElasticBungee plugin;

    public BroadcastCommand(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("Usage: /eb broadcast <message>").color(ChatColor.RED).create());
            return;
        }

        String message = null;

        for (final String arg : args) {
            if (message == null) {
                message = arg;
            } else {
                message += " " + arg;
            }
        }

        this.plugin.getBroadcastSync().broadcast(message);
    }
}
