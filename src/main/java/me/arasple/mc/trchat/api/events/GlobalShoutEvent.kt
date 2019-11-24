package me.arasple.mc.trchat.api.events

import io.izzel.taboolib.module.event.EventCancellable
import io.izzel.taboolib.module.tellraw.TellrawJson
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * @author Arasple
 * @date 2019/8/17 23:08
 */
class GlobalShoutEvent(val player: Player, var message: String?, var format: TellrawJson?) : EventCancellable<GlobalShoutEvent>()