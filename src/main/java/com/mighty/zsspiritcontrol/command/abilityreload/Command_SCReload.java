package com.mighty.zsspiritcontrol.command.abilityreload;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.command.SCCommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class Command_SCReload extends SCCommandBase {
    @Override
    public String getCommandName() {
        return "/screload";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] p_71515_2_) {
        if(!hasPerms(sender))
            return;

        SpiritControl.CONFIG.loadAbilities();
        SpiritControl.reloadPlayerData();
        SpiritControl.LOGGER.info("Reloaded all abilities successfully!");
        sender.addChatMessage(new ChatComponentText("Reloaded all abilities successfully!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
    }
}
