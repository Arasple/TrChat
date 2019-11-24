package me.arasple.mc.trchat.api.events

import io.izzel.taboolib.module.event.EventCancellable
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * @author Arasple
 * @date 2019/8/17 23:08
 */
class PrivateMessageEvent(val player: Player, val to: String, val message: String, val isCrossServer: Boolean) : EventCancellable<PrivateMessageEvent>()
