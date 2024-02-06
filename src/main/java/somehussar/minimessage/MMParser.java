package somehussar.minimessage;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import somehussar.minimessage.util.ComponentBuilder;
import somehussar.minimessage.util.Util;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static somehussar.minimessage.Constants.*;

/**
 * @INFO: BACKPORT OF https://github.com/KyoriPowered/adventure-text-minimessage/tree/MiniMessage-1.0.2
 *
 * Originally made for Bungee
 */
public class MMParser {
    // regex group names
    private static final String START = "start";
    private static final String TOKEN = "token";
    private static final String INNER = "inner";
    private static final String END = "end";
    // https://regex101.com/r/8VZ7uA/5
    private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>([^<>]+)|([^<>]+\"(?<inner>[^\"]+)\"))(?<end>>))+?");

    @Nonnull
    public static String escapeTokens(@Nonnull String richMessage) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            if (startIndex > lastEnd) {
                sb.append(richMessage, lastEnd, startIndex);
            }
            lastEnd = endIndex;

            String start = matcher.group(START);
            String token = matcher.group(TOKEN);
            String inner = matcher.group(INNER);
            String end = matcher.group(END);

            // also escape inner
            if (inner != null) {
                token = token.replace(inner, escapeTokens(inner));
            }

            sb.append("\\").append(start).append(token).append("\\").append(end);
        }

        if (richMessage.length() > lastEnd) {
            sb.append(richMessage.substring(lastEnd));
        }

        return sb.toString();
    }

    @Nonnull
    public static String stripTokens(@Nonnull String richMessage) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            if (startIndex > lastEnd) {
                sb.append(richMessage, lastEnd, startIndex);
            }
            lastEnd = endIndex;
        }

        if (richMessage.length() > lastEnd) {
            sb.append(richMessage.substring(lastEnd));
        }

        return sb.toString();
    }

    @Nonnull
    public static String handlePlaceholders(@Nonnull String richMessage, @Nonnull String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new RuntimeException(
                    "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
        }
        for (int i = 0; i < placeholders.length; i += 2) {
            richMessage = richMessage.replace(TAG_START + placeholders[i] + TAG_END, placeholders[i + 1]);
        }
        return richMessage;
    }

    @Nonnull
    public static IChatComponent getFormat(@Nonnull String richString){
        return Util.fromArray(format(richString));
    }

    @Nonnull
    public static IChatComponent getFormat(@Nonnull String richMessage, @Nonnull String... placeholders){
        return Util.fromArray(format(richMessage, placeholders));
    }

    @Nonnull
    public static IChatComponent getFormat(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders){
        return Util.fromArray(format(richMessage, placeholders));
    }

    @Nonnull
    public static String handlePlaceholders(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            richMessage = richMessage.replace(TAG_START + entry.getKey() + TAG_END, entry.getValue());
        }
        return richMessage;
    }
    @Nonnull
    public static IChatComponent[] format(@Nonnull String richMessage, @Nonnull String... placeholders) {
        return format(handlePlaceholders(richMessage, placeholders));
    }

    @Nonnull
    public static IChatComponent[] format(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        return format(handlePlaceholders(richMessage, placeholders));
    }

    @Nonnull
    public static IChatComponent[] format(@Nonnull String richMessage) {
        ComponentBuilder builder = null;

        Stack<ClickEvent> clickEvents = new Stack<>();
        Stack<HoverEvent> hoverEvents = new Stack<>();
        Stack<EnumChatFormatting> colors = new Stack<>();
        Stack<String> nonTags = new Stack<>();
        EnumSet<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);

        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            String msg = null;
            if (startIndex > lastEnd) {
                msg = richMessage.substring(lastEnd, startIndex);
            }
            lastEnd = endIndex;

            // handle message
            if (msg != null && msg.length() != 0) {
                // append message
                if (builder == null) {
                    builder = new ComponentBuilder(msg);
                } else {
                    if(nonTags.size() > 0) {
                        builder.append(nonTags.pop()+" ", ComponentBuilder.FormatRetention.ALL);
                    }else {
                        builder.append(msg, ComponentBuilder.FormatRetention.NONE);
                    }
                }

                // set everything that is not closed yet
                if (clickEvents.size() > 0) {
                    builder.event(clickEvents.peek());
                }
                if (hoverEvents.size() > 0) {
                    builder.event(hoverEvents.peek());
                }
                if (colors.size() > 0) {
                    builder.color(colors.peek());
                }
                if (decorations.size() > 0) {
                    // no lambda because builder isn't effective final :/
                    for (TextDecoration decor : decorations) {
                        decor.apply(builder);
                    }
                }
            }

//			String group = matcher.group();
//			String start = matcher.group(START);
            String token = matcher.group(TOKEN);
            String inner = matcher.group(INNER);
//			String end = matcher.group(END);

            Optional<TextDecoration> deco;
            Optional<EnumChatFormatting> color;

            // click
            if (token.startsWith(CLICK + SEPARATOR)) {
                clickEvents.push(handleClick(token, inner));
            } else if (token.equals(CLOSE_TAG + CLICK)) {
                clickEvents.pop();
            }
            // hover
            else if (token.startsWith(HOVER + SEPARATOR)) {
                hoverEvents.push(handleHover(token, inner));
            } else if (token.equals(CLOSE_TAG + HOVER)) {
                hoverEvents.pop();
            }
            // decoration
            else if ((deco = resolveDecoration(token)).isPresent()) {
                decorations.add(deco.get());
            } else if (token.startsWith(CLOSE_TAG) && (deco = resolveDecoration(token.replace(CLOSE_TAG, ""))).isPresent()) {
                decorations.remove(deco.get());
            }
            // color
            else if ((color = resolveColor(token)).isPresent()) {
                colors.push(color.get());
            } else if (token.startsWith(CLOSE_TAG) && resolveColor(token.replace(CLOSE_TAG, "")).isPresent()) {
                colors.pop();
            } else {
                // invalid tag
//                SpiritControl.LOGGER.info("Invalid tag: " + TAG_START + token + TAG_END + ". Builder: ");
//                if (builder == null) {
//                    builder = new ComponentBuilder(TAG_START + token + TAG_END);
//                } else {
//                    builder.append(TAG_START + token + TAG_END, ComponentBuilder.FormatRetention.ALL);
//                }
                nonTags.push(TAG_START + token + TAG_END);
            }
        }

        // handle last message part
        if (richMessage.length() > lastEnd) {
            String msg = richMessage.substring(lastEnd);
            // append message
            if (builder == null) {
                builder = new ComponentBuilder(msg);
            } else {
                builder.append(msg, ComponentBuilder.FormatRetention.ALL);
            }

            // set everything that is not closed yet
            if (clickEvents.size() > 0) {
                builder.event(clickEvents.peek());
            }
            if (hoverEvents.size() > 0) {
                builder.event(hoverEvents.peek());
            }
            if (colors.size() > 0) {
                builder.color(colors.peek());
            }
            if (decorations.size() > 0) {
                // no lambda because builder isn't effective final :/
                for (TextDecoration decor : decorations) {
                    decor.apply(builder);
                }
            }
        }

        if (builder == null) {
            // lets just return an empty component
            builder = new ComponentBuilder("");
        }

        return builder.create();
    }

    @Nonnull
    private static ClickEvent handleClick(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new RuntimeException("Can't parse click action (too few args) " + token);
        }
        ClickEvent.Action action = ClickEvent.Action.valueOf(args[1].toUpperCase());
        return new ClickEvent(action, token.replace(CLICK + SEPARATOR + args[1] + SEPARATOR, ""));
    }

    @Nonnull
    private static HoverEvent handleHover(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR, 3);
        if (args.length < 2) {
            throw new RuntimeException("Can't parse hover action (too few args) " + token);
        }
        HoverEvent.Action action = HoverEvent.Action.valueOf(args[1].toUpperCase());
        //For some reason, hover events don't like unformatted text. Dirty fix for that.
        //
        // Will need a rewrite of the whole parser rather than a port.
        IChatComponent comp;
        if(action == HoverEvent.Action.SHOW_TEXT)
            comp = Util.fromArray(format("<white></white>" + inner));
        else
            comp = new ChatComponentText(args[2]);
        return new HoverEvent(action, comp);
    }

    @Nonnull
    private static Optional<EnumChatFormatting> resolveColor(@Nonnull String token) {
        try {
            return Optional.of(EnumChatFormatting.valueOf(token.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Nonnull
    private static Optional<TextDecoration> resolveDecoration(@Nonnull String token) {
        try {
            return Optional.of(TextDecoration.valueOf(token.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    enum TextDecoration {
        BOLD(builder -> builder.bold(true)),
        B(builder -> builder.bold(true)),

        ITALIC(builder -> builder.italic(true)),
        I(builder -> builder.italic(true)),

        UNDERLINE(builder -> builder.underlined(true)),
        U(builder -> builder.underlined(true)),

        STRIKETHROUGH(builder -> builder.strikethrough(true)),
        S(builder -> builder.strikethrough(true)),

        OBFUSCATED(builder -> builder.obfuscated(true)),
        K(builder -> builder.obfuscated(true));

        private Consumer<ComponentBuilder> builder;

        TextDecoration(@Nonnull Consumer<ComponentBuilder> builder) {
            this.builder = builder;
        }

        public void apply(@Nonnull ComponentBuilder comp) {
            builder.accept(comp);
        }
    }
}
