package somehussar.minimessage.util;

import com.mighty.zsspiritcontrol.ability.Ability;
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

    public static String getAbilityHoverValue(Ability ability){
        String name = ability.getName();
        String description = ability.getDescription();

        String inner = "<aqua>"+name+"<gray> - "+description;

        return "<hover:show_text:\""+ inner +"\">"+ability.getId();
    }
}
