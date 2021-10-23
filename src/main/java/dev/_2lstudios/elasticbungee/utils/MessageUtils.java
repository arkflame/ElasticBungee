package dev._2lstudios.elasticbungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class MessageUtils {
    public static void sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', message)).create());
    }
}