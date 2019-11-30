package me.arasple.mc.trchat.chat;

import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.chat.format.Format;
import me.arasple.mc.trchat.chat.obj.ChatType;
import me.arasple.mc.trchat.utils.Js;
import me.arasple.mc.trchat.utils.Notifys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/30 12:09
 */
public class ChatFormats {

    private static HashMap<ChatType, List<Format>> formats = new HashMap<>();

    public static Format getFormat(ChatType type, Player player) {
        return formats.computeIfAbsent(type, x -> new ArrayList<>()).stream().filter(format -> Js.checkCondition(player, format.getRequirement())).findFirst().orElse(null);
    }

    public static void loadFormats(CommandSender... notify) {
        long start = System.currentTimeMillis();
        formats.clear();

        for (ChatType chatType : ChatType.values()) {
            if (TrChatFiles.getFormats().contains(chatType.name())) {
                List<Format> formats = new ArrayList<>();
                TrChatFiles.getFormats().getMapList(chatType.name()).forEach(formatMap -> formats.add(new Format(formatMap)));
                ChatFormats.formats.put(chatType, formats);
            }
        }

        formats.get(ChatType.PRIVATE_RECEIVE).forEach(format -> format.getMsg().setPrivateChat(true));
        formats.get(ChatType.PRIVATE_SEND).forEach(format -> format.getMsg().setPrivateChat(true));

        Notifys.notify(notify, "PLUGIN.LOADED-CHAT-FORMATS", (System.currentTimeMillis() - start));
    }

}
