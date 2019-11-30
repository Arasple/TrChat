package me.arasple.mc.trchat.filter.processer;

import me.arasple.mc.trchat.bstats.Metrics;

import java.util.*;

/**
 * @author andy
 * @version 2.2
 * -
 * https://github.com/andyzty/sensitivewd-filter
 */
public class Filter {

    private static FilterSet SET;
    private static Map<Integer, WordNode> NODES;
    private static Set<Integer> PUNCTUATIONS_SET;
    private static char SIGN;

    public static void setPunctuations(List<String> punctuations) {
        PUNCTUATIONS_SET = new HashSet<>();
        addPunctuations(punctuations);
    }

    public static void setSensitiveWord(List<String> punctuations) {
        SET = new FilterSet();
        NODES = new HashMap<>();
        addSensitiveWord(punctuations);
    }

    public static void setReplacement(char sign) {
        Filter.SIGN = sign;
    }

    private static void addPunctuations(List<String> punctuations) {
        if (!punctuations.isEmpty()) {
            char[] chs;
            for (String curr : punctuations) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    PUNCTUATIONS_SET.add(charConvert(c));
                }
            }
        }
    }

    public static void addSensitiveWord(List<String> words) {
        if (!words.isEmpty()) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode;
            for (String curr : words) {
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                if (SET.contains(fchar)) {
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

    public static FilteredObject doFilter(String src) {
        return doFilter(src, true);
    }

    public static FilteredObject doFilter(String src, boolean filter) {
        if (!filter) {
            return new FilteredObject(src, 0);
        }
        char[] chs = src.toCharArray();
        int length = chs.length, curr, curry, k, count = 0;
        WordNode node;
        for (int i = 0; i < length; i++) {
            curr = charConvert(chs[i]);
            if (SET.contains(curr)) {
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
                count++;
                i = i + markNum;
            }
        }
        Metrics.increase(1, count);
        return new FilteredObject(new String(chs), count);
    }

    private static int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

}
