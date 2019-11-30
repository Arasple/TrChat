package me.arasple.mc.trchat.filter.processer;

public class BCConvert {

    private static final char DBC_CHAR_START = 33;
    private static final char DBC_CHAR_END = 126;
    private static final char SBC_CHAR_START = 65281;
    private static final char SBC_CHAR_END = 65374;
    private static final int CONVERT_STEP = 65248;
    private static final char SBC_SPACE = 12288;
    private static final char DBC_SPACE = ' ';

    public static String bj2qj(String src) {
        if (src == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(src.length());
        char[] ca = src.toCharArray();
        for (char c : ca) {
            if (c == DBC_SPACE) {
                buf.append(SBC_SPACE);
            } else if ((c >= DBC_CHAR_START) && (c <= DBC_CHAR_END)) {
                buf.append((char) (c + CONVERT_STEP));
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static int bj2qj(char src) {
        int r = src;
        if ((src >= DBC_CHAR_START) && (src <= DBC_CHAR_END)) {
            r = src + CONVERT_STEP;
        }
        return r;
    }


    public static String qj2bj(String src) {
        if (src == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(src.length());
        char[] ca = src.toCharArray();
        for (int i = 0; i < src.length(); i++) {
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) {
                buf.append((char) (ca[i] - CONVERT_STEP));
            } else if (ca[i] == SBC_SPACE) {
                buf.append(DBC_SPACE);
            } else {
                buf.append(ca[i]);
            }
        }
        return buf.toString();
    }

    public static int qj2bj(char src) {
        int r = src;
        if (src >= SBC_CHAR_START && src <= SBC_CHAR_END) {
            r = src - CONVERT_STEP;
        } else if (src == SBC_SPACE) {
            r = DBC_SPACE;
        }
        return r;
    }

}