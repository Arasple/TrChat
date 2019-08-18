package me.arasple.mc.litechat.api.events;

import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Arasple
 * @date 2019/8/17 23:08
 */
public class LChatGlobalShoutEvent extends Event implements Cancellable {

    private Player player;
    private String message;
    private TellrawJson format;

    public LChatGlobalShoutEvent(Player player, String message, TellrawJson format) {
        this.player = player;
        this.message = message;
        this.format = format;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

}