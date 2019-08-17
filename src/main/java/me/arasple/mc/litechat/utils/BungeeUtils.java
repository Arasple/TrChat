package me.arasple.mc.litechat.utils;

import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.litechat.LiteChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.Arrays;

import static org.bukkit.Bukkit.getMessenger;

/**
 * @author Arasple
 * @date 2019/8/4 21:23
 */
public class BungeeUtils implements PluginMessageListener {

    private static boolean ENABLE = false;

    public static void setEnable(boolean ENABLE) {
        BungeeUtils.ENABLE = ENABLE;
    }

    public static boolean isEnable() {
        return ENABLE;
    }

    public static void init(Plugin plugin) {
        if (!getMessenger().isOutgoingChannelRegistered(plugin, "BungeeCord")) {
            getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeUtils());
            setEnable(Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord", false));
            TLocale.sendToConsole(isEnable() ? "PLUGIN.REGISTERED-BUNGEE" : "PLUGIN.NONE-BUNGEE");
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!"BungeeCord".equals(channel)) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subChannel = in.readUTF();
            if ("PlayerList".equals(subChannel)) {
                String server = in.readUTF();
                Players.setPlayers(Arrays.asList(in.readUTF().split(", ")));
            }
        } catch (IOException ignored) {
        }
    }

    public static void sendBungeeData(Player player, String... args) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        for (String arg : args) {
            try {
                out.writeUTF(arg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.sendPluginMessage(LiteChat.getInst(), "BungeeCord", byteArray.toByteArray());
    }

}
