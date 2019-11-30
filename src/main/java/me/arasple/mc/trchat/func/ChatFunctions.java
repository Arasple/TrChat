package me.arasple.mc.trchat.func;

import me.arasple.mc.trchat.TrChatFiles;
import me.arasple.mc.trchat.func.imp.Function;
import me.arasple.mc.trchat.utils.Notifys;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/11/30 14:19
 */
public class ChatFunctions {

    private static List<Function> functions = new ArrayList<>();

    public static List<Function> getFunctions() {
        return functions;
    }

    public static void loadFunctions(CommandSender... notify) {
        long start = System.currentTimeMillis();
        functions.clear();

        TrChatFiles.getFunction().getConfigurationSection("CUSTOM").getValues(false).forEach((name, funObj) -> {
            functions.add(new Function(name, (MemorySection) funObj));
        });

        Notifys.notify(notify, "PLUGIN.LOADED-FUNCTIONS", (System.currentTimeMillis() - start));
    }

    public static Function mathFunction(String key) {
        return functions.stream().filter(fun -> fun.getName().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

}
