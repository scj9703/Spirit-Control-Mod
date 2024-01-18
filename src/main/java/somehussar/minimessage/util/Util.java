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
        String name = ability.getId();
        String description = ability.getDescription();

        if(description.contains(" - ")){
            description = "<aqua>"+description;
            description = description.replace(" - ", "<gray> - ");
        }

        return "<hover:show_text:\""+ description +"\">"+name;
    }
}
