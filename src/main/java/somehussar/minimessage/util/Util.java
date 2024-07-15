package somehussar.minimessage.util;

import com.mighty.spiritcontrol.ability.Ability;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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

    public static String getAbilityHover(Ability ability){
        String name = ability.getName();
        String description = ability.getDescription();

        String nameAndID = name+"&r &8[&7"+ability.getId()+"&8]";

        String inner = getItemHoverValue(nameAndID, description.split("\n"));

        return "<hover:show_item:"+ inner +">"+name+"</hover>";
    }

    public static String getItemHoverValue(String name, String... description){
        ItemStack itemStack = new ItemStack(Blocks.stone);

        itemStack.setStackDisplayName(translateAlternateColorCodes('&', "&r&b"+name));
        NBTTagCompound display = itemStack.getTagCompound().getCompoundTag("display");

        NBTTagList loreList = new NBTTagList();
        for(String loreLine : description){
            loreList.appendTag(new NBTTagString(translateAlternateColorCodes('&', "&r&5"+loreLine)));
        }
        display.setTag("Lore", loreList);

        NBTTagCompound itemNbt = new NBTTagCompound();
        itemStack.writeToNBT(itemNbt);

        return itemNbt.toString();
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = '\u00a7';
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }
}
