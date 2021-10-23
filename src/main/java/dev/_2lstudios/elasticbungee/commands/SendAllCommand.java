package dev._2lstudios.elasticbungee.commands;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class SendAllCommand {
    private final ElasticBungee plugin;

    public SendAllCommand(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(new ComponentBuilder("Usage: /eb send <source> <target>").color(ChatColor.RED).create());
            return;
        }

        final String source = args[0];
        final String target = args[1];

        this.plugin.getSendAllSync().sendAllServer(source, target);
        MessageUtils.sendMessage(sender,
                "&eAll players in &b(" + source + ") &esend to &a" + target + " request has been sent to all nodes.");
    }
}
