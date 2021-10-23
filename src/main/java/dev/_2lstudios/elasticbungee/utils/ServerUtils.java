package dev._2lstudios.elasticbungee.utils;

import java.net.InetSocketAddress;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerUtils {

    public static ServerInfo[] getServers() {
        final ProxyServer proxy = ProxyServer.getInstance();
        return (ServerInfo[]) proxy.getServers().values().toArray();
    }

    public static boolean isServerRegistered(final String name) {
        final ProxyServer proxy = ProxyServer.getInstance();
        return proxy.getServers().containsKey(name);
    }

    public static void addServer(final String name, final String addr, final int port, final String motd,
            final boolean restrict) {
        if (isServerRegistered(name)) {
            return;
        }

        final ProxyServer proxy = ProxyServer.getInstance();
        final InetSocketAddress socketAddress = new InetSocketAddress(addr, port);
        final ServerInfo info = proxy.constructServerInfo(name, socketAddress, motd, restrict);
        proxy.getServers().put(name, info);
    }

    public static void addServer(final String name, final String addr, final int port) {
        addServer(name, addr, port, name, false);
    }

    public static String deserializeServer(final ServerInfo server) {
        final InetSocketAddress socket = (InetSocketAddress) server.getSocketAddress();
        return socket.getAddress().toString() + ":" + socket.getPort() + ":" + server.getName();
    }

    public static String deserializeServers() {
        String result = null;

        for (final ServerInfo server : getServers()) {
            if (result == null) {
                result = deserializeServer(server);
            } else {
                result += "," + deserializeServer(server);
            }
        }

        return result;
    }

    public static void serializeServer(final String server) {
        final String[] info = server.split(":");
        addServer(info[2], info[0], Integer.parseInt(info[1]));
    }

    public static void serializeServers(final String deserialized) {
        final String[] deserializedServers = deserialized.split(",");
        for (final String server : deserializedServers) {
            serializeServer(server);
        }
    }
}
