package me.arasple.mc.litechat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Arasple
 * @date 2019/8/17 23:08
 */
public class LChatPrivateMessageEvent extends Event implements Cancellable {

    private Player from;
    private String to;
    private String message;
    private boolean crossServer;

    public LChatPrivateMessageEvent(Player from, String to, String message, boolean crossServer) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.crossServer = crossServer;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
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
