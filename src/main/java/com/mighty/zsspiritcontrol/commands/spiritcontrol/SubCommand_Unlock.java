package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.attack.AbilityDatabase;
import com.mighty.zsspiritcontrol.commands.SubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class SubCommand_Unlock extends SubCommand {
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            throw new WrongUsageException("You don't have the correct permissions!");
        }

        if(args.length > 3){

            throw new WrongUsageException("Too many arguments!");
        }
        if(args.length < 2){
            throw new WrongUsageException("Not enough arguments!");
        }

        EntityPlayerMP player = args.length == 3 ? getPlayer(sender, args[2]) : getCommandSenderAsPlayer(sender);
        String ability = args[1];

        if(!AbilityDatabase.isRegistered(ability)){
            throw new WrongUsageException("Given attack doesn't exist!");
        }

        SCPlayer extPlayer = SCPlayer.getPlayer(player);
        if(!extPlayer.isEnabled()){
            sender.addChatMessage(ChatUtil.getMessage("This player hasn't unlocked spirit control", EnumChatFormatting.RED));
            return;
        }

        extPlayer.addAbility(AbilityDatabase.getAbilityByName(ability));

        player.addChatMessage(ChatUtil.getMessage("You have gained "+ability, EnumChatFormatting.DARK_AQUA));
        if(sender != player){
            sender.addChatMessage(ChatUtil.getMessage(player.getCommandSenderName()+" has gained "+ability, EnumChatFormatting.DARK_AQUA));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            return null;
        }

        if(args.length == 3)
            return getListOfStringMatchingLastWord(args, getPlayers());
        if(args.length == 2){
            return getListOfStringMatchingLastWord(args, AbilityDatabase.getRegisteredNames());
        }

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 3;
    }
}
