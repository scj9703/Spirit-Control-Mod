package com.mighty.zsspiritcontrol.command.spiritcontrol;

import com.mighty.zsspiritcontrol.ability.Attack;
import com.mighty.zsspiritcontrol.ability.PassiveAbility;
import com.mighty.zsspiritcontrol.command.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import somehussar.minimessage.MiniMessageParser;

import java.util.ArrayList;
import java.util.List;

public class SubCommand_Skills extends SCSubCommand {

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

            if(!extPlayer.hasUnlockedSpiritControl()){
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>You haven't learned how to use Spirit Control! Seek training on <dark_purple><bold>Yardrat!"));
                return;
            }

            /**
             * @TODO
             * Revisit this and rewrite it
             */

            ArrayList<Attack> unlockedAttacks = new ArrayList<>(extPlayer.getSuperAttacks());
            unlockedAttacks.addAll(extPlayer.getUltimates());
            ArrayList<PassiveAbility> unlockedPassives = new ArrayList<>(extPlayer.getPassives());
            StringBuilder supers = new StringBuilder("<dark_aqua>Unlocked Super Attacks: <gray>");
            StringBuilder ultimates = new StringBuilder("<dark_aqua>Unlocked Ultimate Attacks: <gray>");
            StringBuilder passives = new StringBuilder("<dark_aqua>Unlocked Passive Attacks: <gray>");
            for (Attack atk:unlockedAttacks){
                if (atk.isUltimate()){
                    ultimates.append(" ").append(atk.getName());
                } else {
                    supers.append(" ").append(atk.getName());
                }
            }
            for (PassiveAbility passive:unlockedPassives){
                passives.append(" ").append(passive.getName());
            }
            sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>These ability names are used in commands such as <aqua>/sc equip."));
            sender.addChatMessage(new ChatComponentText(""));
            sender.addChatMessage(MiniMessageParser.getFormat(supers.toString()));
            sender.addChatMessage(MiniMessageParser.getFormat(ultimates.toString()));
            sender.addChatMessage(MiniMessageParser.getFormat(passives.toString()));

        }else{
            throw new WrongUsageException("You have to be a player to run this command.");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
