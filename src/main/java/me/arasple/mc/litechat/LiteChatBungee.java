package me.arasple.mc.litechat;

import me.arasple.mc.litechat.bstats.MetricsBungee;
import me.arasple.mc.litechat.bungee.ListenerBungeeTransfer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Arasple
 * @date 2019/8/4 22:42
 */
public class LiteChatBungee extends Plugin {

    @Override
    public void onEnable() {
        new MetricsBungee(this);
        getProxy().getPluginManager().registerListener(this, new ListenerBungeeTransfer());
    }

}
