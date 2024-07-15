package com.mighty.spiritcontrol.command;

import net.minecraft.command.ICommandSender;

public abstract class SCSubCommand extends SCCommandBase {

    protected SCCommandBase parent;

    public SCSubCommand(SCCommandBase parent){
        this.parent = parent;
    }

    @Override
    public String getCommandName() {
        return null;
    }

    public abstract String getHelpMessage();

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public SCSubCommand addPerms(Object... perms){
        return (SCSubCommand) super.addPerms(perms);
    }

}
