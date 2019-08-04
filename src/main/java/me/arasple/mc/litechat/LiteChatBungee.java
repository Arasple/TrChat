package me.arasple.mc.litechat;

import me.arasple.mc.litechat.bstats.MetricsBungee;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Arasple
 * @date 2019/8/4 22:42
 */
public class LiteChatBungee extends Plugin implements Listener {

    @Override
    public void onEnable() {
        new MetricsBungee(this);
    }

    @EventHandler
    public void onTransfer(PluginMessageEvent e) {
        try {
            ByteArrayInputStream byteArray = new ByteArrayInputStream(e.getData());
            DataInputStream in = new DataInputStream(byteArray);

            String subChannel = in.readUTF();
            String type = in.readUTF();
            String uuids = in.readUTF();
            String data = in.readUTF();

//            if ("Trixey".equals(subChannel)) {
//                UUID uuid = UUID.fromString(uuids);
//                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
//
//                if (player != null) {
//                    if ("Shout".equals(type)) {
//                        ProxyServer.getInstance().getPlayers().forEach(p -> {
//                            if (TrixeyUserAPI.isVerified(p.getName())) {
//                                p.sendMessage(ComponentSerializer.parse(data));
//                            }
//                        });
//                        return;
//                    }
//                    if ("Staff".equals(type)) {
//                        ProxyServer.getInstance().getPlayers().forEach(p -> {
//                            if (p.hasPermission("trixey.admin")) {
//                                p.sendMessage(ComponentSerializer.parse(data));
//                            }
//                        });
//                    }
//                    if ("Private".equals(type)) {
//                        target = in.readUTF();
//                        if (ProxyServer.getInstance().getPlayer(target) != null && ProxyServer.getInstance().getPlayer(target).isConnected()) {
//                            ProxyServer.getInstance().getPlayer(target).sendMessage(ComponentSerializer.parse(data));
//                        }
//                    }
//                }
//            }
        } catch (IOException ignored) {
        }
    }

}
