package dev._2lstudios.elasticbungee.commands;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class SendCommand {
    private final ElasticBungee plugin;

    public SendCommand(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(new ComponentBuilder("Usage: /eb send <player> <server>").color(ChatColor.RED).create());
            return;
        }

        final String player = args[0];
        final String target = args[1];

        this.plugin.getSendSync().sendServer(player, target);
        MessageUtils.sendMessage(sender,
                "&ePlayer &b(" + player + ") &esend to &a" + target + " request has been sent to all nodes.");
    }
}
