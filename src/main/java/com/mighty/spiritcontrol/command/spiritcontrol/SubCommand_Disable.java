package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import somehussar.minimessage.MMParser;

import java.util.List;

public class SubCommand_Disable extends SCSubCommand {
    public SubCommand_Disable(SCCommandBase parent) {
        super(parent);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender))
            throw new WrongUsageException("You don't have the correct permissions!");


        if(args.length > 2)
            throw new WrongUsageException("Too many arguments!");


        EntityPlayerMP player = args.length == 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);

        SCPlayer extPlayer = SCPlayer.getPlayer(player);
        extPlayer.setUnlockedSpiritControl(false);

        player.addChatMessage(MMParser.getFormat("<dark_aqua>You have forgotten Spirit Control :("));
        if(sender != player){
            sender.addChatMessage(MMParser.getFormat("<gray><player><dark_aqua> has forgotten Spirit Control.", "player", player.getCommandSenderName()));
        }

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            return null;
        }

        if(args.length == 2)
            return getListOfStringsMatchingLastWord(args, getPlayers());
        return null;
    }

    @Override
    public String getHelpMessage() {
        return "<aqua>/"+parent.getCommandName()+" disable [player] <dark_aqua>- locks Spirit Control for a Player";
    }
}
