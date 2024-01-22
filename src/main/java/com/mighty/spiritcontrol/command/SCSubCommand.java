package com.mighty.spiritcontrol.command;

import net.minecraft.command.ICommandSender;

public abstract class SCSubCommand extends SCCommandBase {

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public SCSubCommand addPerms(Object... perms){
        return (SCSubCommand) super.addPerms(perms);
    }

}
