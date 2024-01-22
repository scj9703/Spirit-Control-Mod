package com.mighty.spiritcontrol.command.configreload;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.config.Config;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class Command_SCReload extends SCCommandBase {
    @Override
    public String getCommandName() {
        return "/screload"; //The slash is intentional. It's to prevent you from accidentally running the command
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] p_71515_2_) {
        if(!hasPerms(sender))
            return;


        Config.INSTANCE.loadAbilities();
        Config.INSTANCE.loadMainConfig();
        SpiritControl.reloadPlayerData();
        SpiritControl.LOGGER.info("Reloaded all abilities successfully!");
        sender.addChatMessage(new ChatComponentText("Reloaded all abilities successfully!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
    }
}
