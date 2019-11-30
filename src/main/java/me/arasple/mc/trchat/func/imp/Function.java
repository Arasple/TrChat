package me.arasple.mc.trchat.func.imp;

import me.arasple.mc.trchat.chat.format.objects.JsonComponent;
import org.bukkit.configuration.MemorySection;

import java.util.LinkedHashMap;

/**
 * @author Arasple
 * @date 2019/11/30 14:17
 */
public class Function {

    private String requirement;
    private String name;
    private String pattern;
    private String filterTextPattern;
    private JsonComponent displayJson;

    public Function(String requirement, String name, String pattern, String filterTextPattern, JsonComponent displayJson) {
        this.requirement = requirement;
        this.name = name;
        this.pattern = pattern;
        this.filterTextPattern = filterTextPattern;
        this.displayJson = displayJson;
    }

    public Function(String name, MemorySection funObj) {
        this(funObj.getString("requirement", null), name, funObj.getString("pattern"), funObj.getString("text-filter", null), new JsonComponent((LinkedHashMap) ((MemorySection) funObj.get("display")).getValues(false)));
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFilterTextPattern() {
        return filterTextPattern;
    }

    public void setFilterTextPattern(String filterTextPattern) {
        this.filterTextPattern = filterTextPattern;
    }

    public JsonComponent getDisplayJson() {
        return displayJson;
    }

    public void setDisplayJson(JsonComponent displayJson) {
        this.displayJson = displayJson;
    }
}
