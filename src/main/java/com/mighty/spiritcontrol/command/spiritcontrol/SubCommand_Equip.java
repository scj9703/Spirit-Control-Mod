package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import somehussar.minimessage.MMParser;

import java.util.ArrayList;
import java.util.List;

public class SubCommand_Equip extends SCSubCommand {
    public SubCommand_Equip(SCCommandBase parent) {
        super(parent);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP) {
            SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

            if(!extPlayer.hasUnlockedSpiritControl()){
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>You haven't learned how to use Spirit Control! Seek training on <dark_purple><bold>Yardrat!"));
                return;
            }

            if(args.length > 3)
                throw new WrongUsageException("Too many arguments!");

            if(args.length < 3)
                throw new WrongUsageException("Not enough arguments!");


            Ability ability = AbilityDatabase.getAbilityById(args[2]);
            if(ability == null)
                throw new WrongUsageException("This ability doesn't exist!");

            if(!extPlayer.hasAbility(ability)){
                throw new WrongUsageException("You don't have that ability unlocked!");
            }

            extPlayer.setAbilityAtSlot(ability, args[1]);

        }else{
            throw new WrongUsageException("You have to be a player to use this command.");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!(sender instanceof EntityPlayerMP))
            return null;

        SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

        if(!extPlayer.hasUnlockedSpiritControl())
            return null;


        if(args.length == 2){
            return getListOfStringsMatchingLastWord(args, "Super1", "Super2", "Ultimate", "Passive");
        }

        ArrayList<String> tabCompletion = new ArrayList<>();

        if(args.length == 3){
            switch(args[1].toLowerCase()) {
                case "passive":
                    for(PassiveAbility passive : extPlayer.getUnlockedPassives())
                        tabCompletion.add(passive.getId());
                    break;
                case "ultimate":
                    for(Attack att : extPlayer.getUnlockedUltimates())
                            tabCompletion.add(att.getId());
                    break;
                case "super1":
                case "super2":
                    for(Attack att : extPlayer.getUnlockedSuperAttacks())
                            tabCompletion.add(att.getId());
                    break;
            }
            return getListOfStringsMatchingLastWord(args, tabCompletion.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public String getHelpMessage() {
        return "<aqua>/"+parent.getCommandName()+" equip <slotName> <skillName> <dark_aqua>- equips the selected skill onto your loadout. <aqua>Accepted slot names: <green>Super1, Super2, Ultimate, Passive";
    }
}
