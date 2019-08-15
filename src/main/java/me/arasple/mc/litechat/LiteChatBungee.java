package me.arasple.mc.litechat;

import com.google.common.collect.Lists;
import me.arasple.mc.litechat.bstats.MetricsBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/8/4 22:42
 */
public class LiteChatBungee extends Plugin implements Listener {

    private List<UUID> staffs = Lists.newArrayList();

    @Override
    public void onEnable() {
        new MetricsBungee(this);

        getProxy().getPluginManager().registerListener(this, this);
    }

    public List<UUID> getStaffs() {
        return staffs;
    }

    @EventHandler
    public void onTransfer(PluginMessageEvent e) {
        try {
            ByteArrayInputStream byteArray = new ByteArrayInputStream(e.getData());
            DataInputStream in = new DataInputStream(byteArray);

            String subChannel = in.readUTF();
            String type = in.readUTF();

            if ("LiteChat".equals(subChannel)) {
                if ("SendRaw".equals(type)) {
                    String to = in.readUTF();
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayers().stream().filter(p -> p.getName().equalsIgnoreCase(to)).findFirst().orElse(null);

                    if (player != null && player.isConnected()) {
                        String raw = in.readUTF();
                        player.sendMessage(ComponentSerializer.parse(raw));
                    }
                }
                if ("BroadcastRaw".equals(type)) {
                    String raw = in.readUTF();
                    ProxyServer.getInstance().broadcast(ComponentSerializer.parse(raw));
                }
                if ("SendRawPerm".equals(type)) {
                    String raw = in.readUTF();
                    String perm = in.readUTF();

                    ProxyServer.getInstance().getPlayers().stream().filter(p -> p.hasPermission(perm)).forEach(p -> {
                        p.sendMessage(ComponentSerializer.parse(raw));
                    });
                }
            }
        } catch (IOException ignored) {
        }
    }

}
