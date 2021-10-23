package dev._2lstudios.elasticbungee;

import java.io.File;
import java.util.logging.Level;

import dev._2lstudios.elasticbungee.api.broker.MessageBroker;
import dev._2lstudios.elasticbungee.api.storage.StorageContainer;
import dev._2lstudios.elasticbungee.broker.BrokerFactory;
import dev._2lstudios.elasticbungee.commands.ElasticBungeeCommand;
import dev._2lstudios.elasticbungee.storage.StorageFactory;
import dev._2lstudios.elasticbungee.sync.BroadcastSync;
import dev._2lstudios.elasticbungee.sync.OnlineCountSync;
import dev._2lstudios.elasticbungee.sync.PlayerSync;
import dev._2lstudios.elasticbungee.utils.ConfigUtils;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class ElasticBungee extends Plugin {

    private Configuration mainConfig;

    private boolean debug;
    private String serverID;

    private MessageBroker broker;
    private StorageContainer storage;

    private BroadcastSync broadcastSync;
    private OnlineCountSync onlineCountSync;
    private PlayerSync playerSync;

    public void debug(final String message) {
        if (this.debug) {
            this.getLogger().log(Level.INFO, "§d[DEBUG] §r" + message);
        }
    }

    private void init() throws Exception {
        this.mainConfig = ConfigUtils.getConfig(new File(this.getDataFolder(), "config.yml"));

        // Setup server
        this.debug = this.mainConfig.getBoolean("debug");
        this.serverID = this.mainConfig.getString("server.id");
        this.getLogger().log(Level.INFO, "Defined server id as " + this.serverID);

        // Setup message broker
        final String brokerDriver = this.mainConfig.getString("message-broker.driver");
        final String brokerHost = this.mainConfig.getString("message-broker.host");
        final int brokerPort = this.mainConfig.getInt("message-broker.port");
        final String brokerPassword = this.mainConfig.getString("message-broker.password");

        this.broker = BrokerFactory.craftBroker(brokerDriver, brokerHost, brokerPort, brokerPassword);
        this.getLogger().log(Level.INFO, "Connected to Message broker using driver " + brokerDriver);

        // Setup storage container
        final String storageDriver = this.mainConfig.getString("storage-container.driver");
        final String storageHost = this.mainConfig.getString("storage-container.host");
        final int storagePort = this.mainConfig.getInt("storage-container.port");
        final String storagePassword = this.mainConfig.getString("storage-container.password");

        this.storage = StorageFactory.craftStorage(storageDriver, storageHost, storagePort, storagePassword);
        this.getLogger().log(Level.INFO, "Connected to Storage container using driver " + storageDriver);

        // Setup sync modules
        this.onlineCountSync = new OnlineCountSync(this);
        this.broker.subscribe(OnlineCountSync.CHANNEL, onlineCountSync);
        this.getProxy().getPluginManager().registerListener(this, onlineCountSync);
        this.getLogger().log(Level.INFO, "Registered module OnlineCountSync");

        this.playerSync = new PlayerSync(this);
        this.getProxy().getPluginManager().registerListener(this, playerSync);
        this.getLogger().log(Level.INFO, "Registered module PlayerSync");

        this.broadcastSync = new BroadcastSync(this);
        this.broker.subscribe(BroadcastSync.CHANNEL, broadcastSync);
        this.getLogger().log(Level.INFO, "Registered module BroadcastSync");

        // Register commands
        this.getProxy().getPluginManager().registerCommand(this, new ElasticBungeeCommand(this));
        this.getLogger().log(Level.INFO, "Registered elasticbungee command");
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            this.init();
        } catch (final Exception e) {
            this.getLogger().log(Level.SEVERE,
                    "ElasticBungee had an unexpected error and the proxy will be closed to prevent security breaches.");
            e.printStackTrace();
            this.getProxy().stop();
        }
    }

    public BroadcastSync getBroadcastSync() {
        return this.broadcastSync;
    }

    public MessageBroker getMessageBroker() {
        return this.broker;
    }

    public StorageContainer getStorage() {
        return this.storage;
    }

    public OnlineCountSync getOnlineCountSync() {
        return this.onlineCountSync;
    }

    public PlayerSync getPlayerSync() {
        return this.playerSync;
    }

    public String getServerID() {
        return this.serverID;
    }

    /* Static Values */
    private static ElasticBungee instance = null;

    public static ElasticBungee getInstance() {
        return instance;
    }

}
