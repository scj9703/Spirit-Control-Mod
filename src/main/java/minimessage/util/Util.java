package minimessage.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Util {
    public static IChatComponent fromArray(IChatComponent... extras){
        if(extras == null)
            return null;

        if(extras.length == 1){
            return extras[0];
        }
        ChatComponentText componentText = new ChatComponentText("");
        for(IChatComponent toAdd : extras){
            componentText.getSiblings().add(toAdd);
        }
        return componentText;
    }
}
