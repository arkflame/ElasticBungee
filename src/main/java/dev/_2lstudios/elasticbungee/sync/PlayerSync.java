package dev._2lstudios.elasticbungee.sync;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.sync.results.PlayerSyncResult;
import dev._2lstudios.elasticbungee.utils.MessageUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerSync implements Listener {

    private static final String QUERY_PREFIX = "eb_user_";

    private final ElasticBungee plugin;

    public PlayerSync(final ElasticBungee plugin) {
        this.plugin = plugin;
    }

    private void updatePlayerData(final String username, final String server) {
        final String key = QUERY_PREFIX + username.toLowerCase();

        if (server == null || server.isEmpty()) {
            this.plugin.getStorage().delete(key);
            return;
        }

        final PlayerSyncResult result = new PlayerSyncResult(this.plugin.getServerID(), server);
        this.plugin.getStorage().set(key, result.toString());
    }

    public void sync() {
        for (final ProxiedPlayer player : this.plugin.getProxy().getPlayers()) {
            this.updatePlayerData(player.getName(), player.getServer().getInfo().getName());
        }
    }

    public void cleanup() {
        for (final ProxiedPlayer player : this.plugin.getProxy().getPlayers()) {
            this.updatePlayerData(player.getName(), null);
        }
    }

    public PlayerSyncResult getPlayer(final String username) {
        final String data = this.plugin.getStorage().get(QUERY_PREFIX + username.toLowerCase());
        if (data != null) {
            return PlayerSyncResult.fromString(data);
        } else {
            return null;
        }
    }

    @EventHandler
    public void onPlayerPreLogin(final PreLoginEvent e) {
        if (this.getPlayer(e.getConnection().getName()) != null) {
            final BaseComponent[] reason = MessageUtils
                    .format(this.plugin.getConfig().getString("messages.already-connected"));
            e.setCancelReason(reason);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerConnected(final ServerConnectedEvent e) {
        this.updatePlayerData(e.getPlayer().getName(), e.getServer().getInfo().getName());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerDisconnectEvent e) {
        this.updatePlayerData(e.getPlayer().getName(), null);
    }

    @EventHandler
    public void onPlayerKick(final ServerKickEvent e) {
        this.updatePlayerData(e.getPlayer().getName(), null);
    }
}
