package me.arasple.mc.litechat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Arasple
 * @date 2019/8/17 23:08
 */
public class PrivateMessageEvent extends PlayerEvent implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private String to;
    private String message;
    private boolean crossServer;
    private boolean canceled;

    public PrivateMessageEvent(Player from, String to, String message, boolean crossServer) {
        super(from);
        this.to = to;
        this.message = message;
        this.crossServer = crossServer;
        this.canceled = false;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCrossServer() {
        return crossServer;
    }

    public void setCrossServer(boolean crossServer) {
        this.crossServer = crossServer;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        canceled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
