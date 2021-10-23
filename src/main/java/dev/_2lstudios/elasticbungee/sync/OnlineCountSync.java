package dev._2lstudios.elasticbungee.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dev._2lstudios.elasticbungee.ElasticBungee;
import dev._2lstudios.elasticbungee.api.broker.Message;
import dev._2lstudios.elasticbungee.api.broker.Subscription;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnlineCountSync implements Listener, Subscription {
    public final static String CHANNEL = "sync";

    private final ElasticBungee plugin;
    private final Map<String, OnlineCountSyncResult> serverPlayerData;

    public OnlineCountSync(final ElasticBungee plugin) {
        this.plugin = plugin;
        this.serverPlayerData = new HashMap<>();

        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                requestRemoteSync();

                plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                    @Override
                    public void run() {
                        syncLocalData();
                    }
                }, 1, 10, TimeUnit.SECONDS);
            }

        }, 1, TimeUnit.SECONDS);
    }

    /* Server player data utils */
    private boolean hasServer(final String serverID) {
        return this.serverPlayerData.containsKey(serverID);
    }

    private OnlineCountSyncResult getServer(final String serverID) {
        return this.serverPlayerData.get(serverID);
    }

    private void requestRemoteSync() {
        this.plugin.debug("PubSub Sent: request_sync_players");
        this.plugin.getMessageBroker().publish(CHANNEL, "request_sync_players");
    }

    /* Local sync utils */
    private void syncLocalData() {
        this.plugin.debug("PubSub Sent: sync_players");
        this.plugin.getMessageBroker().publish(CHANNEL, "sync_players:" + this.plugin.getProxy().getOnlineCount());
    }

    private void addLocalPlayer() {
        this.plugin.debug("PubSub Sent: add_player");
        this.plugin.getMessageBroker().publish(CHANNEL, "add_player");
    }

    private void removeLocalPlayer() {
        this.plugin.debug("PubSub Sent: remove_player");
        this.plugin.getMessageBroker().publish(CHANNEL, "remove_player");
    }

    /* Remove sync utils */
    private void syncRemoteData(final String serverID, final int count) {
        if (this.hasServer(serverID)) {
            this.getServer(serverID).setPlayerCount(count);
        } else {
            this.serverPlayerData.put(serverID, new OnlineCountSyncResult(count));
        }
    }

    private void addRemotePlayer(final String serverID) {
        if (this.hasServer(serverID)) {
            this.getServer(serverID).addPlayer();
        } else {
            this.syncRemoteData(serverID, 1);
        }
    }

    private void removeRemotePlayer(final String serverID) {
        if (this.hasServer(serverID)) {
            this.getServer(serverID).removePlayer();
        }
    }

    /* Broker listener */
    @Override
    public void onReceive(final Message message) {
        if (!message.getChannel().equals(CHANNEL)) {
            return;
        }

        final boolean isLocal = message.getSource().equals(this.plugin.getServerID());
        if (isLocal) {
            return;
        }

        this.plugin.debug("PubSub Recv: SRC=" + message.getSource() + ", CNT=" + message.getContent());

        if (message.getContent().equals("add_player")) {
            this.addRemotePlayer(message.getSource());
        }

        else if (message.getContent().equals("remove_player")) {
            this.removeRemotePlayer(message.getSource());
        }

        else if (message.getContent().equals("request_sync_players")) {
            this.syncLocalData();
        }

        else if (message.getContent().startsWith("sync_players:") && !isLocal) {
            final int count = Integer.parseInt(message.getContent().split(":")[1]);
            this.syncRemoteData(message.getSource(), count);
        }
    }

    /* Ping utils */
    public int getTotalPlayerCount() {
        int output = this.plugin.getProxy().getOnlineCount();

        for (final OnlineCountSyncResult result : this.serverPlayerData.values()) {
            output += result.getPlayerCount();
        }

        return output;
    }

    /* Events */
    @EventHandler
    public void onProxyPing(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        ServerPing.Players current = ping.getPlayers();

        ping.setPlayers(new ServerPing.Players(current.getMax(), this.getTotalPlayerCount(), current.getSample()));
        e.setResponse(ping);
    }

    @EventHandler
    public void onPostLogin(final PostLoginEvent e) {
        this.addLocalPlayer();
    }

    @EventHandler
    public void onPlayerQuit(final PlayerDisconnectEvent e) {
        this.removeLocalPlayer();
    }
}
