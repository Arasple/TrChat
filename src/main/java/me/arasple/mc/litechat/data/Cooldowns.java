package me.arasple.mc.litechat.data;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.serialize.TSerializable;
import io.izzel.taboolib.util.serialize.TSerializeCollection;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/15 18:38
 */
public class Cooldowns implements TSerializable {


    @TSerializeCollection
    private List<Cooldown> cooldowns = Lists.newArrayList();

    public Cooldowns() {

    }

    public List<Cooldown> getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(List<Cooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }


    public static class Cooldown implements TSerializable {

        private String id;
        private long time;

        public Cooldown() {

        }

        public Cooldown(String id, long time) {
            this.id = id;
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

    }

    public enum CooldownType {

        /**
         * Chat Cooldown Types
         */

        CHAT("Chat"),
        ITEM_SHOW("ItemShow");

        private String name;

        CooldownType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
