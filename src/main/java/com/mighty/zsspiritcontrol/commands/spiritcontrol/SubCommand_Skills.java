package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.attack.Attack;
import com.mighty.zsspiritcontrol.attack.PassiveAbility;
import com.mighty.zsspiritcontrol.commands.SubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class SubCommand_Skills extends SubCommand {

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

            if(!extPlayer.isEnabled()){
                sender.addChatMessage(ChatUtil.getMessage("You haven't learned how to use Spirit Control! Seek training on \u00a75\u00a7lYardrat!", EnumChatFormatting.AQUA));
                return;
            }

            /**
             * @TODO
             * Revisit this and rewrite it
             */

            ArrayList<Attack> unlockedAttacks = new ArrayList<>(extPlayer.getAttacks());
            unlockedAttacks.addAll(extPlayer.getUltimates());
            ArrayList<PassiveAbility> unlockedPassives = new ArrayList<>(extPlayer.getPassives());
            String supers = "Unlocked Super Attacks:";
            String ultimates = "Unlocked Ultimate Attacks:";
            String passives = "Unlocked Passive Attacks:";
            for (Attack atk:unlockedAttacks){
                if (atk.isUltimate()){
                    ultimates += " " + atk.getName();
                } else {
                    supers += " " + atk.getName();
                }
            }
            for (PassiveAbility passive:unlockedPassives){
                passives += " " + passive.getName();
            }
            sender.addChatMessage(ChatUtil.getMessage("These ability names are used in commands such as /sc equip.", EnumChatFormatting.AQUA));
            sender.addChatMessage(ChatUtil.getMessage(supers, EnumChatFormatting.AQUA));
            sender.addChatMessage(ChatUtil.getMessage(ultimates, EnumChatFormatting.AQUA));
            sender.addChatMessage(ChatUtil.getMessage(passives, EnumChatFormatting.AQUA));

        }else{
            sender.addChatMessage(ChatUtil.getMessage("You have to be a player to run this command.", EnumChatFormatting.RED));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
