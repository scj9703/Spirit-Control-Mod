package com.mighty.zsspiritcontrol.command;

import net.minecraft.command.ICommandSender;

public abstract class SCSubCommand extends SCCommandBase {

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return null;
    }

    @Override
    public SCSubCommand addPerms(Object... perms){
        return (SCSubCommand) super.addPerms(perms);
    }

}
