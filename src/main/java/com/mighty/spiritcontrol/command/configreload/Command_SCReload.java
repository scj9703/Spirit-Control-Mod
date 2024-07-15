package com.mighty.spiritcontrol.command.configreload;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.config.Config;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.List;

public class Command_SCReload extends SCCommandBase {
    @Override
    public String getCommandName() {
        return "screload";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender))
            throw new WrongUsageException("You don't have the correct permissions to run this command");

        if(args.length < 1 || !args[0].equalsIgnoreCase("true"))
            throw new WrongUsageException("If you're really sure about this, you need to run /screload true");

        Config.INSTANCE.loadAbilities();
        Config.INSTANCE.loadMainConfig();
        SpiritControl.reloadPlayerData();
        SpiritControl.LOGGER.info("Reloaded all abilities successfully!");
        sender.addChatMessage(new ChatComponentText("Reloaded all abilities successfully!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 1)
            return Collections.singletonList("true");
        return null;
    }
}
