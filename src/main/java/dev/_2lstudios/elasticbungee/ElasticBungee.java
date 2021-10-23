package dev._2lstudios.elasticbungee;

import java.io.File;
import java.util.logging.Level;

import dev._2lstudios.elasticbungee.commands.ElasticBungeeCommand;
import dev._2lstudios.elasticbungee.redis.RedisMessageBroker;
import dev._2lstudios.elasticbungee.redis.RedisStorage;
import dev._2lstudios.elasticbungee.sync.BroadcastSync;
import dev._2lstudios.elasticbungee.sync.OnlineCountSync;
import dev._2lstudios.elasticbungee.sync.PlayerSync;
import dev._2lstudios.elasticbungee.utils.ConfigUtils;
import dev._2lstudios.elasticbungee.utils.StringUtils;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class ElasticBungee extends Plugin {
    private Configuration mainConfig;

    private boolean debug;
    private String serverID;

    private RedisMessageBroker broker;
    private RedisStorage storage;

    private BroadcastSync broadcastSync;
    private OnlineCountSync onlineCountSync;
    private PlayerSync playerSync;

    public void debug(final String message) {
        if (this.debug) {
            this.getLogger().log(Level.INFO, "§d[DEBUG] §r" + message);
        }
    }

    private void init() throws Exception {
        final File configFile = new File(this.getDataFolder(), "config.yml");
        this.mainConfig = ConfigUtils.getConfig(configFile);

        // Setup server
        this.debug = this.mainConfig.getBoolean("debug");
        this.serverID = this.mainConfig.getString("server.id", "bungee-" + StringUtils.randomString(6));
        this.getLogger().log(Level.INFO, "Defined server id as " + this.serverID);

        // Setup message broker and storage
        final String redisHost = this.mainConfig.getString("redis.host");
        final int redisPort = this.mainConfig.getInt("redis.port");
        final String redisPassword = this.mainConfig.getString("redis.password");

        this.broker = new RedisMessageBroker(redisHost, redisPort, redisPassword);
        this.getLogger().log(Level.INFO, "Connected to redis Message broker");

        this.storage = new RedisStorage(redisHost, redisPort, redisPassword);
        this.getLogger().log(Level.INFO, "Connected to redis Storage Container");

        // Setup sync modules
        this.onlineCountSync = new OnlineCountSync(this);
        this.broker.subscribe(onlineCountSync);
        this.getProxy().getPluginManager().registerListener(this, onlineCountSync);
        this.getLogger().log(Level.INFO, "Registered module OnlineCountSync");

        this.playerSync = new PlayerSync(this);
        this.getProxy().getPluginManager().registerListener(this, playerSync);
        this.getLogger().log(Level.INFO, "Registered module PlayerSync");

        this.broadcastSync = new BroadcastSync(this);
        this.broker.subscribe(broadcastSync);
        this.getLogger().log(Level.INFO, "Registered module BroadcastSync");

        // Register commands
        this.getProxy().getPluginManager().registerCommand(this, new ElasticBungeeCommand(this));
        this.getLogger().log(Level.INFO, "Registered elasticbungee command");

        // Initialize if there are players online
        this.playerSync.sync();

        // Save config after initialize
        ConfigUtils.saveConfig(this.mainConfig, configFile);
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

    @Override
    public void onDisable() {
        this.playerSync.cleanup();
    }

    public Configuration getConfig() {
        return this.mainConfig;
    }

    public BroadcastSync getBroadcastSync() {
        return this.broadcastSync;
    }

    public RedisMessageBroker getMessageBroker() {
        return this.broker;
    }

    public RedisStorage getStorage() {
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
