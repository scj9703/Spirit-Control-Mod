package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.attack.AbilityDatabase;
import com.mighty.zsspiritcontrol.attack.Attack;
import com.mighty.zsspiritcontrol.attack.PassiveAbility;
import com.mighty.zsspiritcontrol.commands.SubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class SubCommand_Equip extends SubCommand {
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP) {
            SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

            if (!extPlayer.isEnabled()) {
                sender.addChatMessage(ChatUtil.getMessage("You haven't learned how to use Spirit Control! Seek training on \u00a75\u00a7lYardrat!", EnumChatFormatting.AQUA));
                return;
            }

            if(args.length > 3){
                throw new WrongUsageException("Too many arguments!");
            }
            if(args.length < 3){
                throw new WrongUsageException("Not enough arguments!");
            }

            String abilityName = args[2];
            if(!AbilityDatabase.isRegistered(abilityName)){
                throw new WrongUsageException("That ability doesn't exist!");
            }

            List<Attack> playerAttacks = new ArrayList<>(extPlayer.getAttacks());
            List<PassiveAbility> playerPassives = new ArrayList<>(extPlayer.getPassives());

            Attack attack = (Attack) AbilityDatabase.getAbilityByName(abilityName);
            PassiveAbility passive = (PassiveAbility) AbilityDatabase.getAbilityByName(abilityName);

            if(!(playerPassives.contains(passive) || playerAttacks.contains(attack))){
                throw new WrongUsageException("You don't have that ability unlocked!");
            }

            switch(args[1].toLowerCase()){
                case "passive":
                    if(passive == null){
                        throw new WrongUsageException("That passive doesn't exist!");
                    }
                    extPlayer.setAbilityAtSlot(passive, "passive");
                    sender.addChatMessage(ChatUtil.getMessage("Equipped "+passive.getName()+"as Passive", EnumChatFormatting.DARK_AQUA));
                    break;
                case "ultimate":
                    if(attack == null || !attack.isUltimate()){
                        throw new WrongUsageException("That ultimate doesn't exist!");
                    }
                    extPlayer.setAbilityAtSlot(attack, "ultimate");
                    sender.addChatMessage(ChatUtil.getMessage("Equipped "+attack.getName()+" as Ultimate", EnumChatFormatting.DARK_AQUA));
                    break;
                case "super1":
                    if(attack == null || attack.isUltimate()){
                        throw new WrongUsageException("That attack doesn't exist!");
                    }
                    extPlayer.setAbilityAtSlot(attack, "super1");
                    sender.addChatMessage(ChatUtil.getMessage("Equipped "+attack.getName()+" on slot 1", EnumChatFormatting.DARK_AQUA));
                    break;
                case "super2":
                    if(attack == null || attack.isUltimate()){
                        throw new WrongUsageException("That attack doesn't exist!");
                    }
                    extPlayer.setAbilityAtSlot(attack, "super2");
                    sender.addChatMessage(ChatUtil.getMessage("Equipped "+attack.getName()+" on slot 2", EnumChatFormatting.DARK_AQUA));
                    break;
                default:
                    throw new WrongUsageException("That slot doesn't exist!");
            }

        }else{
            sender.addChatMessage(ChatUtil.getMessage("You have to be a player to run this command.", EnumChatFormatting.RED));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!(sender instanceof EntityPlayerMP))
            return null;

        SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

        if(!extPlayer.isEnabled())
            return null;


        if(args.length == 2){
            return getListOfStringMatchingLastWord(args, "Super1", "Super2", "Ultimate", "Passive");
        }

        ArrayList<String> tabCompletion = new ArrayList<>();

        if(args.length == 3){
            switch(args[1].toLowerCase()) {
                case "passive":
                    for(PassiveAbility passive : extPlayer.getPassives()){
                        tabCompletion.add(passive.getName());
                    }
                    break;
                case "ultimate":
                    for(Attack att : extPlayer.getUltimates()){
                            tabCompletion.add(att.getName());
                    }
                    break;
                case "super1":
                case "super2":
                    for(Attack att : extPlayer.getAttacks()){
                            tabCompletion.add(att.getName());
                    }
                    break;
            }
            return getListOfStringMatchingLastWord(args, tabCompletion.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
