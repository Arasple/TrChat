package me.arasple.mc.litechat.filter;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.litechat.LiteChat;

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
        SIGN = LiteChat.getSettings().getString("CHAT-CONTROL.FILTER.REPLACEMENT", "*").charAt(0);
        addSensitiveWord(LiteChat.getSettings().getStringList("CHAT-CONTROL.FILTER.SENSITIVE-WORDS"));
        addPunctuations(LiteChat.getSettings().getStringList("CHAT-CONTROL.FILTER.IGNORED-PUNCTUATIONS"));
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

    public static String doFilter(String src, boolean filter) {
        if (!filter) {
            return src;
        }
        char[] chs = src.toCharArray();
        int length = chs.length;
        int curr;
        int curry;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            curr = charConvert(chs[i]);
            if (!SET.contains(curr)) {
                continue;
            }
            node = NODES.get(curr);
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
            curry = curr;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (temp == curry) {
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
                curry = temp;
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

    public static int getContainsAmount(final String src) {
        char[] chs = src.toCharArray();
        int length = chs.length;
        int curr;
        int curry;
        int k;
        WordNode node;
        int count = 0;
        for (int i = 0; i < length; i++) {
            curr = charConvert(chs[i]);
            if (!SET.contains(curr)) {
                continue;
            }
            node = NODES.get(curr);
            if (node == null) {
                continue;
            }
            boolean couldMark = false;
            if (node.isLast()) {
                couldMark = true;
            }

            k = i;
            curry = curr;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (temp == curry) {
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
                curry = temp;
            }
            if (couldMark) {
                count++;
            }
        }
        return count;
    }

    private static int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

    public static <T> boolean isEmpty(final Collection<T> col) {
        return col == null || col.isEmpty();
    }

}
