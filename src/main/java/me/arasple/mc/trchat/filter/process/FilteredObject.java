package me.arasple.mc.trchat.filter.process;

/**
 * @author Arasple
 * @date 2019/9/12 17:40
 */
public class FilteredObject {

    private String filtered;
    private int sensitiveWords;

    public FilteredObject(String filtered, int sensitiveWords) {
        this.filtered = filtered;
        this.sensitiveWords = sensitiveWords;
    }

    public String getFiltered() {
        return filtered;
    }

    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    public int getSensitiveWords() {
        return sensitiveWords;
    }

    public void setSensitiveWords(int sensitiveWords) {
        this.sensitiveWords = sensitiveWords;
    }
}
