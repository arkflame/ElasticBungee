package dev._2lstudios.elasticbungee.api.events.player;

import dev._2lstudios.elasticbungee.api.events.ElasticPlayerEvent;

public class PlayerLeaveEvent extends ElasticPlayerEvent {
    public PlayerLeaveEvent(final String proxyID, final String username) {
        super(proxyID, username);
    }
}
