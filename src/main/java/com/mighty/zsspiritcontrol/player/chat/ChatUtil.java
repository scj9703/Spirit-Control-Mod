package com.mighty.zsspiritcontrol.player.chat;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatUtil {

    public static IChatComponent getMessage(String msg, EnumChatFormatting color){
        return getMessage(msg, new ChatStyle().setColor(color));
    }
    public static IChatComponent getMessage(String msg, ChatStyle style){
        return new ChatComponentText(msg).setChatStyle(style);
    }

}
