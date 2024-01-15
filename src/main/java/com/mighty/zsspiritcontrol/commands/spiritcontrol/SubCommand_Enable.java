package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class SubCommand_Enable extends SubCommand {

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
        extPlayer.toggleSpiritControl(true);

        player.addChatMessage(ChatUtil.getMessage("You have gained Spirit Control :)", EnumChatFormatting.DARK_AQUA));
        if(sender != player){
            sender.addChatMessage(ChatUtil.getMessage(player.getCommandSenderName()+" has gained Spirit Control.", EnumChatFormatting.DARK_AQUA));
        }

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            return null;
        }

        if(args.length == 2)
            return getListOfStringMatchingLastWord(args, getPlayers());
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}

