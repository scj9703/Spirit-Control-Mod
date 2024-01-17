package somehussar.minimessage.util;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentBuilder {

    private int cursor = -1;
    private final List<IChatComponent> parts = new ArrayList<>();
    private IChatComponent dummy;

    public ComponentBuilder(String text) {
        this(new ChatComponentText(text));
    }

    public ComponentBuilder(IChatComponent chatComponentText) {
        this(new IChatComponent[]{chatComponentText});
    }

    public ComponentBuilder(IChatComponent[] parts) {
        for(IChatComponent base : parts){
            this.parts.add(base.createCopy());
        }
        this.resetCursor();
    }

    private ComponentBuilder resetCursor() {
        cursor = parts.size()-1;
        return this;
    }
    public IChatComponent getCurrentComponent(){
        return (cursor == -1) ? getDummy() : parts.get(cursor);
    }

    private IChatComponent getDummy() {
        if(dummy == null){
            dummy = new ChatComponentText(""){
                @Override
                public ChatComponentText createCopy(){
                    return this;
                }
            };
        }

        return dummy;
    }

    public ComponentBuilder bold(boolean b) {
        getCurrentComponent().getChatStyle().setBold(b);
        return this;
    }

    public ComponentBuilder italic(boolean b) {
        getCurrentComponent().getChatStyle().setItalic(b);
        return this;
    }

    public ComponentBuilder underlined(boolean b) {
        getCurrentComponent().getChatStyle().setUnderlined(b);
        return this;
    }

    public ComponentBuilder strikethrough(boolean b) {
        getCurrentComponent().getChatStyle().setStrikethrough(b);
        return this;
    }

    public ComponentBuilder obfuscated(boolean b) {
        getCurrentComponent().getChatStyle().setObfuscated(b);
        return this;
    }
    public ComponentBuilder event(ClickEvent peek) {
        getCurrentComponent().getChatStyle().setChatClickEvent(peek);
        return this;
    }

    public ComponentBuilder event(HoverEvent peek) {
        getCurrentComponent().getChatStyle().setChatHoverEvent(peek);
        return this;
    }

    public ComponentBuilder color(EnumChatFormatting peek) {
        if(peek.isColor())
            getCurrentComponent().getChatStyle().setColor(peek);
        return this;
    }

    public ComponentBuilder append(String msg, FormatRetention formatRetention) {

        return append(new ChatComponentText(msg), formatRetention);
    }

    private ComponentBuilder append(IChatComponent component, FormatRetention formatRetention) {
        IChatComponent previous = parts.isEmpty() ? null : parts.get(parts.size()-1);
        if(previous == null){
            previous = dummy;
            dummy = null;
        }
        if(previous != null && !component.getChatStyle().isEmpty() ){
            copyFormatting(component, previous, formatRetention, false);
        }

        parts.add(component);
        resetCursor();
        return this;
    }

    private static void copyFormatting(IChatComponent component, IChatComponent previous, FormatRetention retention, boolean replace) {
        ChatStyle style = component.getChatStyle();
        ChatStyle previousStyle = previous.getChatStyle();
        if ( retention == FormatRetention.EVENTS || retention == FormatRetention.ALL )
        {
            if ( replace || style.getChatClickEvent() == null )
            {
                style.setChatClickEvent( previousStyle.getChatClickEvent() );
            }
            if ( replace || style.getChatHoverEvent() == null )
            {
                style.setChatHoverEvent( previousStyle.getChatHoverEvent() );
            }
        }
        if ( retention == FormatRetention.FORMATTING || retention == FormatRetention.ALL )
        {
            if ( replace || style.getColor() == null )
            {
                style.setColor( previousStyle.getColor() );
            }
            if ( replace || style.getBold()  )
            {
                style.setBold( previousStyle.getBold() );
            }
            if ( replace || style.getItalic()  )
            {
                style.setItalic( previousStyle.getItalic() );
            }
            if ( replace || style.getUnderlined())
            {
                style.setUnderlined( previousStyle.getUnderlined() );
            }
            if ( replace || style.getStrikethrough()  )
            {
                style.setStrikethrough( previousStyle.getStrikethrough() );
            }
            if ( replace || style.getObfuscated()  )
            {
                style.setObfuscated( previousStyle.getObfuscated() );
            }
        }

    }

    public IChatComponent[] create() {
        IChatComponent[] cloned = new IChatComponent[parts.size()];
        int i = 0;
        for(IChatComponent part : parts){
            cloned[i++] = part.createCopy();
        }
        return cloned;
    }

    public enum FormatRetention
    {

        /**
         * Specify that we do not want to retain anything from the previous
         * component.
         */
        NONE,
        /**
         * Specify that we want the formatting retained from the previous
         * component.
         */
        FORMATTING,
        /**
         * Specify that we want the events retained from the previous component.
         */
        EVENTS,
        /**
         * Specify that we want to retain everything from the previous
         * component.
         */
        ALL
    }

    /**
     * Functional interface to join additional components to a ComponentBuilder.
     */
    public interface Joiner
    {

        /**
         * Joins additional components to the provided {@link ComponentBuilder}
         * and then returns it to fulfill a chain pattern.
         *
         * Retention may be ignored and is to be understood as an optional
         * recommendation to the Joiner and not as a guarantee to have a
         * previous component in builder unmodified.
         *
         * @param componentBuilder to which to append additional components
         * @param retention the formatting to possibly retain
         * @return input componentBuilder for chaining
         */
        ComponentBuilder join(ComponentBuilder componentBuilder, FormatRetention retention);
    }
}
