package dev._2lstudios.elasticbungee.commands;

import java.util.Arrays;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ElasticBungeeCommand extends Command {

    private final BroadcastCommand broadcastCommand;
    private final FindCommand findCommand;
    private final KickCommand kickCommand;
    private final IPCommand ipCommand;

    private final String version;

    public ElasticBungeeCommand(final ElasticBungee plugin) {
        super("elasticbungee", "elasticbungee.use", "eb");

        this.broadcastCommand = new BroadcastCommand(plugin);
        this.findCommand = new FindCommand(plugin);
        this.kickCommand = new KickCommand(plugin);
        this.ipCommand = new IPCommand(plugin);

        this.version = plugin.getDescription().getVersion();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
            MessageUtils.sendMessage(sender, "");
            MessageUtils.sendMessage(sender, "&6&lElastic&e&lBungee &8(" + this.version + ")");
            MessageUtils.sendMessage(sender, "&c/eb broadcast <message> &8- &7send a message to all proxies.");
            MessageUtils.sendMessage(sender, "&c/eb find <player> &8- &7Search to which server a user is connected.");
            MessageUtils.sendMessage(sender, "&c/eb kick <player> &8- Kick a player.");
            MessageUtils.sendMessage(sender, "&c/eb ip <player> &8- Get a player's ip.");
            MessageUtils.sendMessage(sender, "&c/eb send <player> <server> &8- Send a player to a server.");
            MessageUtils.sendMessage(sender,
                    "&c/eb sendall <source> <target> &8- Send all players from one server to another.");
            MessageUtils.sendMessage(sender, "");
            return;
        }

        final String subCommand = args[0];
        final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (subCommand) {
        case "broadcast":
            this.broadcastCommand.execute(sender, subArgs);
            break;
        case "find":
            this.findCommand.execute(sender, subArgs);
            break;
        case "kick":
            this.kickCommand.execute(sender, subArgs);
            break;
        case "ip":
            this.ipCommand.execute(sender, subArgs);
            break;
        default:
            MessageUtils.sendMessage(sender, "&cError: Subcommand " + subCommand + "doesn't exist.");
        }
    }
}
