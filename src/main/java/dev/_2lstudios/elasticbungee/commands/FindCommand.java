package dev._2lstudios.elasticbungee.commands;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.sync.results.PlayerSyncResult;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class FindCommand {
    private final ElasticBungee plugin;

    public FindCommand(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                    new ComponentBuilder("Please follow this command by a user name.").color(ChatColor.RED).create());
            return;
        }

        final String username = args[0];
        final PlayerSyncResult player = this.plugin.getPlayerSync().getPlayer(username);

        if (player == null) {
            sender.sendMessage(new ComponentBuilder("That user is not online.").color(ChatColor.RED).create());
        } else {
            sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + username + ChatColor.RESET + " is online in "
                    + player.getServerName() + " " + ChatColor.AQUA + "(" + player.getProxyID() + ")").create());
        }
    }
}
