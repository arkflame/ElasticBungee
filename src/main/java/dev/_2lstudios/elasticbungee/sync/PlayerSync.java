package dev._2lstudios.elasticbungee.sync;

import dev._2lstudios.elasticbungee.ElasticBungee;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
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
        }

        final PlayerSyncResult result = new PlayerSyncResult(this.plugin.getServerID(), server);
        this.plugin.getStorage().set(key, result.toString());
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
    public void onPostLogin(final ServerConnectEvent e) {
        this.updatePlayerData(e.getPlayer().getName(), e.getTarget().getName());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerDisconnectEvent e) {
        this.updatePlayerData(e.getPlayer().getName(), null);
    }
}
