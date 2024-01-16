package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.attack.Attack;
import com.mighty.zsspiritcontrol.attack.PassiveAbility;
import com.mighty.zsspiritcontrol.commands.SubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class SubCommand_Check extends SubCommand {

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            throw new WrongUsageException("You don't have the correct permissions!");
        }

        if(args.length > 2){

            throw new WrongUsageException("Too many arguments!");
        }
        if(args.length < 2){
            throw new WrongUsageException("Not enough arguments!");
        }

        /**
         * @TODO
         * Revisit and rewrite this
         */

        EntityPlayerMP player = getPlayer(sender, args[1]);
        SCPlayer extPlayer = SCPlayer.getPlayer(player);

        double gauge = extPlayer.getSpirit();
        double cap = extPlayer.getMaxSpirit();
        Attack superAttack1 = (Attack) extPlayer.getAbilityFromSlot("super1");
        Attack superAttack2 = (Attack) extPlayer.getAbilityFromSlot("super2");
        Attack ultimate = (Attack) extPlayer.getAbilityFromSlot("ultimate");
        PassiveAbility passiveAbility = (PassiveAbility) extPlayer.getAbilityFromSlot("passive");
        ArrayList<Attack> unlockedAttacks = new ArrayList<>(extPlayer.getAttacks());
        unlockedAttacks.addAll(extPlayer.getUltimates());
        ArrayList<PassiveAbility> unlockedPassives = (ArrayList<PassiveAbility>) extPlayer.getPassives();

        sender.addChatMessage(ChatUtil.getMessage("Checking player " + player.getCommandSenderName(), EnumChatFormatting.GRAY));
        sender.addChatMessage(ChatUtil.getMessage("Spirit Gauge: " + gauge + " out of " + cap + " Spirit", EnumChatFormatting.GRAY));
        sender.addChatMessage(ChatUtil.getMessage("Super1: " + superAttack1.getName() + " Super2: " + superAttack2.getName(), EnumChatFormatting.GRAY));
        sender.addChatMessage(ChatUtil.getMessage("Ultimate: " + ultimate.getName() + " Passive: " + passiveAbility.getName(), EnumChatFormatting.GRAY));
        String attackList = "Unlocked Attacks:";
        String passiveList = "Unlocked Passives:";
        for (Attack atk:unlockedAttacks){
            attackList += " " + atk.getName();
        }
        for (PassiveAbility passive:unlockedPassives){
            passiveList += " " + passive.getName();
        }
        sender.addChatMessage(ChatUtil.getMessage(attackList, EnumChatFormatting.GRAY));
        sender.addChatMessage(ChatUtil.getMessage(passiveList, EnumChatFormatting.GRAY));

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
        return index == 2;
    }
}
