package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.attack.Ability;
import com.mighty.zsspiritcontrol.attack.AbilityDatabase;
import com.mighty.zsspiritcontrol.commands.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import somehussar.minimessage.MiniMessageParser;

import java.util.List;

public class SubCommand_Lock extends SCSubCommand {
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
        Ability ability = AbilityDatabase.getAbilityByName(args[1]);

        if(ability == null){
            throw new WrongUsageException("Given attack doesn't exist!");
        }

        SCPlayer extPlayer = SCPlayer.getPlayer(player);
        if(!extPlayer.isEnabled()){
            throw new WrongUsageException("This player has not learned Spirit Control yet.");
        }

        extPlayer.removeAbility(ability);

        player.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>You have forgotten <aqua><ability>", "ability", ability.getName()));
        if(sender != player){
            sender.addChatMessage(MiniMessageParser.getFormat("<aqua><player><dark_aqua> has forgotten <aqua><ability>", "player", player.getCommandSenderName(), "ability", ability.getName()));
        }

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            return null;
        }

        if(args.length == 3)
            return getListOfStringsMatchingLastWord(args, getPlayers());
        if(args.length == 2){
            return getListOfStringsMatchingLastWord(args, AbilityDatabase.getRegisteredNames());
        }

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 3;
    }
}
