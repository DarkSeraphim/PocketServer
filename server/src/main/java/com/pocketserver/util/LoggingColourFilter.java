package com.pocketserver.util;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.pocketserver.api.ChatColor;
import org.fusesource.jansi.Ansi;
import org.slf4j.Marker;

/**
 * @author Connor Spencer Harries
 */
public class LoggingColourFilter extends TurboFilter {
    private static final Pattern COLOUR_PATTERN = Pattern.compile("\u00A7([a-f0-9k-pr])", Pattern.CASE_INSENSITIVE);
    private static final String RESET_STRING = Ansi.ansi().a(Ansi.Attribute.RESET).toString();
    private static final Map<Character, String> replacementMap;

    static {
        final Ansi ansi = Ansi.ansi().a(Ansi.Attribute.RESET);
        replacementMap = ImmutableMap.copyOf(new HashMap<Character, String>() {{
            put('a', ansi.fg(Ansi.Color.GREEN).bold().toString());
            put('b', ansi.fg(Ansi.Color.CYAN).bold().toString());
            put('c', ansi.fg(Ansi.Color.RED).boldOff().toString());
            put('d', ansi.fg(Ansi.Color.MAGENTA).boldOff().toString());
            put('e', ansi.fg(Ansi.Color.YELLOW).boldOff().toString());
            put('f', ansi.fg(Ansi.Color.WHITE).bold().toString());
            put('0', ansi.fg(Ansi.Color.BLACK).boldOff().toString());
            put('1', ansi.fg(Ansi.Color.BLUE).bold().toString());
            put('2', ansi.fg(Ansi.Color.GREEN).bold().toString());
            put('3', ansi.fg(Ansi.Color.CYAN).bold().toString());
            put('4', ansi.fg(Ansi.Color.RED).bold().toString());
            put('5', ansi.fg(Ansi.Color.MAGENTA).bold().toString());
            put('6', ansi.fg(Ansi.Color.YELLOW).bold().toString());
            put('7', ansi.fg(Ansi.Color.WHITE).boldOff().toString());
            put('8', ansi.fg(Ansi.Color.BLACK).bold().toString());
            put('9', ansi.fg(Ansi.Color.BLUE).boldOff().toString());
            put('r', RESET_STRING);
        }});
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    continue;
                }

                if (params[i].getClass() == ChatColor.class) {
                    ChatColor color = (ChatColor) params[i];
                    params[i] = replacementMap.getOrDefault(color.getCharacter(), "");
                }
            }
        }

        if (format != null) {
            Matcher matcher = COLOUR_PATTERN.matcher(format);
            boolean found = matcher.find();
            if (found) {
                StringBuilder buffer = new StringBuilder();
                int position = 0;
                while (found) {
                    char code = Character.toLowerCase(matcher.group(1).charAt(0));
                    buffer.append(format.substring(position, matcher.start()));
                    buffer.append(replacementMap.getOrDefault(code, ""));
                    position = matcher.end();
                    found = matcher.find();
                }

                if (position < format.length()) {
                    buffer.append(format.substring(position, format.length()));
                }

                format = buffer.append(RESET_STRING).toString();
                if (level == Level.TRACE) {
                    logger.trace(marker, format, params);
                } else if (level == Level.DEBUG) {
                    logger.debug(marker, format, params);
                } else if (level == Level.INFO) {
                    logger.info(marker, format, params);
                } else if (level == Level.WARN) {
                    logger.warn(marker, format, params);
                } else if (level == Level.ERROR) {
                    logger.error(marker, format, params);
                }
                return FilterReply.DENY;
            }
        }
        return FilterReply.NEUTRAL;
    }
}
