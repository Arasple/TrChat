package me.arasple.mc.trchat.nms;

/**
 * @author Arasple
 * @date 2019/11/30 11:17
 */
public abstract class AbstractPacketUtils {

    /**
     * 过滤 IChatBaseComponent 中的敏感词
     *
     * @param component 对象
     * @return 过滤后的
     */
    public abstract Object filterIChatComponent(Object component);

    public abstract void filterItem(Object c);

    public abstract void filterItemList(Object b);

    public abstract boolean isAvailable();

}
