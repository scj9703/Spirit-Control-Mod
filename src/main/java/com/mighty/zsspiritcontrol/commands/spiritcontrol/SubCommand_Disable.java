package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import somehussar.minimessage.MiniMessageParser;

import java.util.List;

public class SubCommand_Disable extends SCSubCommand {
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            throw new WrongUsageException("You don't have the correct permissions!");
        }

        if(args.length > 2){

            throw new WrongUsageException("Too many arguments!");
        }

        EntityPlayerMP player = args.length == 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);

        SCPlayer extPlayer = SCPlayer.getPlayer(player);
        extPlayer.setUnlockedSpiritControl(false);

        player.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>You have forgotten Spirit Control :("));
        if(sender != player){
            sender.addChatMessage(MiniMessageParser.getFormat("<gray><player><dark_aqua> has forgotten Spirit Control.", "player", player.getCommandSenderName()));
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
}
