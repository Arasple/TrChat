package me.arasple.mc.litechat.filter;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.litechat.LCFiles;

import java.util.*;

/**
 * @author andy
 * @version 2.2
 * <p>
 * https://github.com/andyzty/sensitivewd-filter
 */
public class WordFilter {

    private static final FilterSet SET = new FilterSet();
    private static final Map<Integer, WordNode> NODES = new HashMap<>(1024, 1);
    private static final Set<Integer> PUNCTUATIONS_SET = new HashSet<>();
    private static char SIGN;

    @TSchedule(delay = 3)
    public static void loadSettings() {
        SIGN = LCFiles.getSettings().getString("ChatControl.filter.replacement", "*").charAt(0);
        addSensitiveWord(LCFiles.getSettings().getStringList("ChatControl.filter.sensitive-words"));
        addPunctuations(LCFiles.getSettings().getStringList("ChatControl.filter.ignored-punctuations"));
    }

    private static void addPunctuations(final List<String> words) {
        if (!isEmpty(words)) {
            char[] chs;
            for (String curr : words) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    PUNCTUATIONS_SET.add(charConvert(c));
                }
            }
        }
    }

    private static void addSensitiveWord(final List<String> words) {
        if (!isEmpty(words)) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode;
            for (String curr : words) {
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                if (!SET.contains(fchar)) {
                    SET.add(fchar);
                    fnode = new WordNode(fchar, chs.length == 1);
                    NODES.put(fchar, fnode);
                } else {
                    fnode = NODES.get(fchar);
                    if (!fnode.isLast() && chs.length == 1) {
                        fnode.setLast(true);
                    }
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    public static String doFilter(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int cpcurrc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!SET.contains(currc)) {
                continue;
            }
            node = NODES.get(currc);
            if (node == null) {
                continue;
            }
            boolean couldMark = false;
            int markNum = -1;
            if (node.isLast()) {
                couldMark = true;
                markNum = 0;
            }
            k = i;
            cpcurrc = currc;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (temp == cpcurrc) {
                    continue;
                }
                if (PUNCTUATIONS_SET.contains(temp)) {
                    continue;
                }
                node = node.querySub(temp);
                if (node == null) {
                    break;
                }
                if (node.isLast()) {
                    couldMark = true;
                    markNum = k - i;
                }
                cpcurrc = temp;
            }
            if (couldMark) {
                for (k = 0; k <= markNum; k++) {
                    if (!PUNCTUATIONS_SET.contains(charConvert(chs[k + i]))) {
                        chs[k + i] = SIGN;
                    }
                }
                i = i + markNum;
            }
        }
        return new String(chs);

    }

    public static boolean isContains(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int cpcurrc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!SET.contains(currc)) {
                continue;
            }
            node = NODES.get(currc);
            if (node == null) {
                continue;
            }
            boolean couldMark = false;
            if (node.isLast()) {
                couldMark = true;
            }


            k = i;
            cpcurrc = currc;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (temp == cpcurrc) {
                    continue;
                }
                if (PUNCTUATIONS_SET.contains(temp)) {
                    continue;
                }
                node = node.querySub(temp);
                if (node == null) {
                    break;
                }
                if (node.isLast()) {
                    couldMark = true;
                }
                cpcurrc = temp;
            }
            if (couldMark) {
                return true;
            }
        }
        return false;
    }

    private static int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

    public static <T> boolean isEmpty(final Collection<T> col) {
        return col == null || col.isEmpty();
    }

}
