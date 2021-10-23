package dev._2lstudios.elasticbungee.api.events.player;

import dev._2lstudios.elasticbungee.api.events.ElasticPlayerEvent;

public class PlayerJoinedEvent extends ElasticPlayerEvent {
    public PlayerJoinedEvent(final String proxyID, final String username) {
        super(proxyID, username);
    }
}
