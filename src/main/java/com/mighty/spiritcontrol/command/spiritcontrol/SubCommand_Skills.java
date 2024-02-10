package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import somehussar.minimessage.MMParser;

import java.util.List;
import java.util.Set;

public class SubCommand_Skills extends SCSubCommand {

    public SubCommand_Skills(SCCommandBase parent) {
        super(parent);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

            if(!extPlayer.hasUnlockedSpiritControl()){
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>You haven't learned how to use Spirit Control! Seek training on <dark_purple><bold>Yardrat!"));
                return;
            }

            /**
             * @TODO
             * Revisit this and rewrite it (Maybe?)
             */
            Set<Attack> unlockedAttacks = extPlayer.getUnlockedSuperAttacks();
            unlockedAttacks.addAll(extPlayer.getUnlockedUltimates());
            Set<PassiveAbility> unlockedPassives = extPlayer.getUnlockedPassives();
            StringBuilder supers = new StringBuilder("<dark_aqua>Unlocked Super Attacks: <gray>");
            StringBuilder ultimates = new StringBuilder("<dark_aqua>Unlocked Ultimate Attacks: <gray>");
            StringBuilder passives = new StringBuilder("<dark_aqua>Unlocked Passive Attacks: <gray>");
            for (Attack atk:unlockedAttacks){
                if (atk.isUltimate()){
                    ultimates.append(" ").append(atk.getId());
                } else {
                    supers.append(" ").append(atk.getId());
                }
            }
            for (PassiveAbility passive:unlockedPassives){
                passives.append(" ").append(passive.getId());
            }
            sender.addChatMessage(MMParser.getFormat("<dark_aqua>These ability names are used in commands such as <aqua>/"+parent.getCommandName()+" equip"));
            sender.addChatMessage(new ChatComponentText(""));
            sender.addChatMessage(MMParser.getFormat(supers.toString()));
            sender.addChatMessage(MMParser.getFormat(ultimates.toString()));
            sender.addChatMessage(MMParser.getFormat(passives.toString()));

        }else{
            throw new WrongUsageException("You have to be a player to run this command.");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getHelpMessage() {
        return "<aqua>/"+parent.getCommandName()+" skills <dark_aqua>- displays all unlocked skill names and descriptions";
    }
}
