package me.arasple.mc.litechat.utils;

import io.izzel.taboolib.module.inject.TSchedule;
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
public class Bungees implements PluginMessageListener {

    private static boolean ENABLE = false;

    @TSchedule
    public static void init() {
        Plugin plugin = LiteChat.getPlugin();
        if (!getMessenger().isOutgoingChannelRegistered(plugin, "BungeeCord")) {
            getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new Bungees());
            setEnable(Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord", false));
            TLocale.sendToConsole(isEnable() ? "PLUGIN.REGISTERED-BUNGEE" : "PLUGIN.NONE-BUNGEE");
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
        player.sendPluginMessage(LiteChat.getPlugin(), "BungeeCord", byteArray.toByteArray());
    }

    public static boolean isEnable() {
        return ENABLE;
    }

    /*
    GETTERS & SETTERS
     */

    public static void setEnable(boolean ENABLE) {
        Bungees.ENABLE = ENABLE;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel == null || player == null || message == null) {
            return;
        }
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

}
