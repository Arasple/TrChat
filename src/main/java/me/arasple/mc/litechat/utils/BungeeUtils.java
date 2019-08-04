package me.arasple.mc.litechat.utils;

import me.arasple.mc.litechat.LiteChat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/8/4 21:23
 */
public class BungeeUtils implements PluginMessageListener {

    private static boolean enable = false;

    public static void setEnable(boolean enable) {
        BungeeUtils.enable = enable;
    }

    public static boolean isEnable() {
        return enable;
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
        player.sendPluginMessage(LiteChat.getPlugin(), "BungeeCord", byteArray.toByteArray());
    }

}
