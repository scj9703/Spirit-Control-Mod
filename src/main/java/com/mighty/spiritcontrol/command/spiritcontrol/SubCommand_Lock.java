package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import somehussar.minimessage.MMParser;
import somehussar.minimessage.util.Util;

import java.util.List;

public class SubCommand_Lock extends SCSubCommand {
    public SubCommand_Lock(SCCommandBase parent) {
        super(parent);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender))
            throw new WrongUsageException("You don't have the correct permissions!");


        if(args.length > 3)
            throw new WrongUsageException("Too many arguments!");

        if(args.length < 2)
            throw new WrongUsageException("Not enough arguments!");


        EntityPlayerMP player = args.length == 3 ? getPlayer(sender, args[2]) : getCommandSenderAsPlayer(sender);
        Ability ability = AbilityDatabase.getAbilityById(args[1]);

        if(ability == null){
            throw new WrongUsageException("Given attack doesn't exist!");
        }

        SCPlayer extPlayer = SCPlayer.getPlayer(player);
        if(!extPlayer.hasUnlockedSpiritControl()){
            throw new WrongUsageException("This player has not learned Spirit Control yet.");
        }

        extPlayer.removeAbility(ability);

        player.addChatMessage(MMParser.getFormat("<dark_aqua>You have forgotten <aqua><ability>", "ability", Util.getAbilityHover(ability)));
        if(sender != player){
            sender.addChatMessage(MMParser.getFormat("<aqua><player><dark_aqua> has forgotten <aqua><ability>", "player", player.getCommandSenderName(), "ability", Util.getAbilityHover(ability)));
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
            return getListOfStringsMatchingLastWord(args, AbilityDatabase.getRegisteredIds());
        }

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 3;
    }

    @Override
    public String getHelpMessage() {
        return "<aqua>/"+parent.getCommandName()+" lock <skillName> [player] <dark_aqua>- removes an ability";
    }
}
