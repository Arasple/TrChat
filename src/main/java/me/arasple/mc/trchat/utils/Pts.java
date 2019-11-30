package me.arasple.mc.trchat.utils;

import io.izzel.taboolib.util.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arasple
 * @date 2019/11/30 14:30
 */
public class Pts {

    public static String replacePattern(String string, String pattern, String textPattern, String replacement) {
        Matcher matcher = Pattern.compile(pattern).matcher(string);
        while (matcher.find()) {
            String str = matcher.group();
            Matcher m = textPattern != null ? Pattern.compile(textPattern).matcher(str) : null;
            String rep = Strings.replaceWithOrder(replacement, textPattern == null ? str : m != null && m.find() ? m.group() : str);
            string = string.replaceAll(str, rep);
        }
        return string;
    }

}
