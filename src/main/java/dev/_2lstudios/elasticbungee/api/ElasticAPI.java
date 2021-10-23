package dev._2lstudios.elasticbungee.api;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.redis.RedisSubscription;
import dev._2lstudios.elasticbungee.sync.results.PlayerSyncResult;

public class ElasticAPI {

    private final ElasticBungee plugin;
    private static ElasticAPI api;

    public ElasticAPI(final ElasticBungee plugin) {
        this.plugin = plugin;
        api = this;
    }

    public ElasticAPI getAPI() {
        return ElasticAPI.api;
    }

    /* Message Broker Methods */
    public void subscribe(final RedisSubscription subscription) {
        this.plugin.getMessageBroker().subscribe(subscription);
    }

    public void publish(final String channel, final String content) {
        this.plugin.getMessageBroker().publish(channel, content);
    }

    /* Sync module methods */
    public void broadcast(final String message) {
        this.plugin.getBroadcastSync().broadcast(message);
    }

    public void kickPlayer(final String username, final String reason) {
        this.plugin.getKickSync().kick(username, reason);
    }

    public PlayerSyncResult getPlayer(final String username) {
        return this.plugin.getPlayerSync().getPlayer(username);
    }

    public String getPlayerAddress(final String username) {
        final PlayerSyncResult result = this.getPlayer(username);
        return result != null ? result.getAddress() : null;
    }

    public String getPlayerServerName(final String username) {
        final PlayerSyncResult result = this.getPlayer(username);
        return result != null ? result.getServerName() : null;
    }

    public String getPlayerProxy(final String username) {
        final PlayerSyncResult result = this.getPlayer(username);
        return result != null ? result.getProxyID() : null;
    }

    public int getOnlineCount() {
        return this.plugin.getOnlineCountSync().getTotalPlayerCount();
    }

    public void send(final String player, final String targetServer) {
        this.plugin.getSendSync().sendServer(player, targetServer);
    }

    public void sendAll(final String sourceServer, final String targetServer) {
        this.plugin.getSendAllSync().sendAllServer(sourceServer, targetServer);
    }

}
