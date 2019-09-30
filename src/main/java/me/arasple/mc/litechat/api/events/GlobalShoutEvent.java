package me.arasple.mc.litechat.api.events;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Arasple
 * @date 2019/8/17 23:08
 */
public class GlobalShoutEvent extends PlayerEvent implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private String message;
    private TellrawJson format;
    private boolean canceled;

    public GlobalShoutEvent(Player player, String message, TellrawJson format) {
        super(player);
        this.message = message;
        this.format = format;
        this.canceled = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TellrawJson getFormat() {
        return format;
    }

    public void setFormat(TellrawJson format) {
        this.format = format;
    }

    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return !((Cancellable) this).isCancelled();
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}