package com.mighty.zsspiritcontrol.command.spiritcontrol;

import com.mighty.zsspiritcontrol.ability.Attack;
import com.mighty.zsspiritcontrol.ability.PassiveAbility;
import com.mighty.zsspiritcontrol.command.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import somehussar.minimessage.MiniMessageParser;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubCommand_Check extends SCSubCommand {

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
        double cap = extPlayer.getSpirit();
        String percent = new DecimalFormat("#.##").format(gauge/cap);
        Attack superAttack1 = (Attack) extPlayer.getAbilityFromSlot("super1");
        Attack superAttack2 = (Attack) extPlayer.getAbilityFromSlot("super2");
        Attack ultimate = (Attack) extPlayer.getAbilityFromSlot("ultimate");
        PassiveAbility passiveAbility = (PassiveAbility) extPlayer.getAbilityFromSlot("passive");
        Set<Attack> unlockedAttacks = new HashSet<>(extPlayer.getUltimates());
        unlockedAttacks.addAll(extPlayer.getUltimates());
        Set<PassiveAbility> unlockedPassives = extPlayer.getPassives();

        sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Checking player: <gray>" + player.getCommandSenderName()));
        sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Spirit Gauge: <gray><gauge></gray> out of <gray><max_gauge> (<percent>%)</gray> Spirit", "gauge", String.valueOf(gauge), "max_gauge", String.valueOf(cap), "percent", percent));
        sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Super1: <gray><ability1></gray>, Super2: <gray><ability2></gray>", "ability1", superAttack1.getName(), "ability2", superAttack2.getName()));
        sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Ultimate: <gray><ability1></gray>, Passive: <gray><ability2></gray>", "ability1", ultimate.getName(), "ability2", passiveAbility.getName()));
        String attackList = "<dark_aqua>Unlocked Attacks:<gray>";
        String passiveList = "<dark_aqua>Unlocked Passives:<gray>";
        for (Attack atk:unlockedAttacks){
            attackList += " " + atk.getName();
        }
        for (PassiveAbility passive:unlockedPassives){
            passiveList += " " + passive.getName();
        }
        sender.addChatMessage(MiniMessageParser.getFormat(attackList));
        sender.addChatMessage(MiniMessageParser.getFormat(passiveList));

    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            return null;
        }

        if(args.length == 2)
            return getListOfStringsMatchingLastWord(args, getPlayers());
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 2;
    }
}
