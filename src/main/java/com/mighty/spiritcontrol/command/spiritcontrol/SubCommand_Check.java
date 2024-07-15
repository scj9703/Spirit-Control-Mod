package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import somehussar.minimessage.MMParser;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class SubCommand_Check extends SCSubCommand {

    public SubCommand_Check(SCCommandBase parent) {
        super(parent);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!hasPerms(sender)){
            throw new WrongUsageException("You don't have the correct permissions!");
        }

        if(args.length > 2)
            throw new WrongUsageException("Too many arguments!");

        if(args.length < 2)
            throw new WrongUsageException("Not enough arguments!");


        /**
         * @TODO
         * Revisit and rewrite this
         */

        EntityPlayerMP player = getPlayer(sender, args[1]);
        SCPlayer extPlayer = SCPlayer.getPlayer(player);

        double gauge = extPlayer.getSpirit();
        double cap = extPlayer.getMaxSpirit();
        String percent = new DecimalFormat("#.##").format(gauge/cap * 100);
        Attack superAttack1 = (Attack) extPlayer.getAbilityFromSlot("super1");
        Attack superAttack2 = (Attack) extPlayer.getAbilityFromSlot("super2");
        Attack ultimate = (Attack) extPlayer.getAbilityFromSlot("ultimate");
        PassiveAbility passiveAbility = (PassiveAbility) extPlayer.getAbilityFromSlot("passive");

        Set<Attack> unlockedAttacks = extPlayer.getUnlockedSuperAttacks();
        Set<Attack> unlockedUltimates = extPlayer.getUnlockedUltimates();
        Set<PassiveAbility> unlockedPassives = extPlayer.getUnlockedPassives();

        DecimalFormat format = new DecimalFormat("#.##");

        sender.addChatMessage(MMParser.getFormat("<dark_aqua>Checking player: <gray>" + player.getCommandSenderName()));
        sender.addChatMessage(MMParser.getFormat("<dark_aqua>Spirit Gauge: <gray><gauge></gray> out of <gray><max_gauge> (<percent>%)</gray> Spirit", "gauge", format.format(gauge), "max_gauge", format.format(cap), "percent", percent));
        sender.addChatMessage(MMParser.getFormat("<dark_aqua>Super1: <gray><ability1></gray>, Super2: <gray><ability2></gray>", "ability1", superAttack1.getId(), "ability2", superAttack2.getId()));
        sender.addChatMessage(MMParser.getFormat("<dark_aqua>Ultimate: <gray><ability1></gray>, Passive: <gray><ability2></gray>", "ability1", ultimate.getId(), "ability2", passiveAbility.getId()));
        StringBuilder attackList = new StringBuilder("<dark_aqua>Unlocked Attacks:<gray>");
        StringBuilder ultimateList = new StringBuilder("<dark_aqua>Unlocked Ultimates:<gray>");
        StringBuilder passiveList = new StringBuilder("<dark_aqua>Unlocked Passives:<gray>");
        for (Attack atk : unlockedAttacks){
            attackList.append(" ").append(atk.getId());
        }
        for(Attack atk : unlockedUltimates){
            ultimateList.append(" ").append(atk.getId());
        }
        for (PassiveAbility passive : unlockedPassives){
            passiveList.append(" ").append(passive.getId());
        }
        sender.addChatMessage(MMParser.getFormat(attackList.toString()));
        sender.addChatMessage(MMParser.getFormat(ultimateList.toString()));
        sender.addChatMessage(MMParser.getFormat(passiveList.toString()));

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

    @Override
    public String getHelpMessage() {
        return "<aqua>/"+parent.getCommandName()+" check [player] <dark_aqua>- shows a Player's Gauge, load-out, and all unlocked abilities";
    }
}
