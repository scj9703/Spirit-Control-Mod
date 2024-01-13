package com.mighty.commands;
import java.lang.reflect.Array;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mighty.zsspiritcontrol.AbilityDatabase;
import com.mighty.zsspiritcontrol.Attack;
import com.mighty.zsspiritcontrol.PassiveAbility;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import com.mighty.util.SCPlayer;
import org.lwjgl.Sys;

public class commandsc extends CommandBase {

    /**
     * Contains the various sub-commands, with a descriptor for how they work
     * Format: '[subcommand] [arguments (separated by spaces)] - [sub-command
     * descriptor]' If the sub-command is only usable by operators 'OP' is added at
     * the end of the descriptor
     */
    String[] commandList = {
            "help - shows a list of commands",
            "skills - displays all unlocked skill names and descriptions",
            "equip [slotName] [skillName] - equips the selected skill onto your loadout",
            "enable [player] - unlocks Spirit Control for a Player OP",
            "disable [player] - locks Spirit Control for a Player OP",
            "check [player] - shows a Player’s Gauge, loadout, and all unlocked abilities OP",
            "unlock [skillName] [player] - unlocks an ability OP",
            "lock [skillName] [player] - removes an ability OP" };

    @Override
    public String getCommandName() {
        return "sc";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "sc";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());

        SCPlayer ex = player != null ? SCPlayer.getPlayer(player) : null;

        if (args.length == 0) {
            subCom(player, ex);
        } else if (process(player, listContains(args[0]),
                "That is not a correct sub command or one hasn't been inputted!")) {

            if (args[0].equalsIgnoreCase("help")) {
                subComHelp(player);
            } else if (args[0].equalsIgnoreCase("unlock")) {
                subComUnlock(player, args[1], args[2]);
            } else if (args[0].equalsIgnoreCase("lock")) {
                subComLock(player, args[1], args[2]);
            } else if (args[0].equalsIgnoreCase("equip")) {
                subComEquip(player, args[1], args[2]);
            }

        }
    }

    /**
     * Fires if player inputs no sub-commands
     * "sc" displays the player's Gauge and Loadout.
     * @param player
     */
    private void subCom(EntityPlayer player, SCPlayer ex) {
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.GOLD + "Spirit Control cmd is a WIP!"));
        double gauge = ex.getCurrGauge();
        double cap = ex.getGaugeCapacity();
        double percent = (gauge / cap) * 100;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedPercent = decimalFormat.format(percent);
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> Your Spirit Control Loadout"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> " + ex.printGauge() + " Your Spirit Gauge is at " + formattedPercent + " Percent Capacity."));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> Super Attack 1: " + ex.getSuperAttack1().getDesc()));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> Super Attack 2: " + ex.getSuperAttack2().getDesc()));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> Ultimate Attack: " + ex.getUltimateAttack().getDesc()));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> Passive Ability: " + ex.getPassiveAbility().getDesc()));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + ">"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> Do /sc help for a full list of commands!"));
        player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
    }

    /**
     * The sub-command for displaying the sub-commands to the player
     *
     * @param player
     */
    private void subComHelp(EntityPlayer player) {
        for (String s : commandList) {
            if (isOp(player) || !isOpCommand(s)) {
                player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "" + s));
            }
        }
    }

    private void subComEquip(EntityPlayer player, String loadoutSlot, String ability){
        if (player != null){
            SCPlayer ex = SCPlayer.getPlayer(player);
            AbilityDatabase abilityDatabase = new AbilityDatabase();
            ArrayList<Attack> playerAttacks = ex.getAttacks();
            ArrayList<PassiveAbility> playerPassives = ex.getPassives();
            Attack attackToEquip = abilityDatabase.getAttackByName(ability);
            PassiveAbility passiveToEquip = abilityDatabase.getPassiveByName(ability);
            if (loadoutSlot.equals("Super1")){
                for (Attack attack : playerAttacks) {
                    if (attack.getName().equals(ability)) {
                        ex.setSuperAttack1(attack);
                    }
                }
                subCom(player, ex);
            } else if (loadoutSlot.equals("Super2")) {
                for (Attack attack : playerAttacks) {
                    if (attack.getName().equals(ability)) {
                        ex.setSuperAttack2(attack);
                    }
                }
                subCom(player, ex);
            } else if (loadoutSlot.equals("Ultimate")) {
                for (Attack attack : playerAttacks) {
                    if (attack.getName().equals(ability)) {
                        ex.setUltimateAttack(attack);
                    }
                }
                subCom(player, ex);
            } else if (loadoutSlot.equals("Passive")) {
                for (PassiveAbility passive:playerPassives){
                    if (passive.getName().equals(ability)){
                        ex.setPassiveAbility(passive);
                    }
                }
                subCom(player, ex);
            } else {
                player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "Error: Valid slotNames are: Super1, Super2, Ultimate, Passive. Note capitalization."));
            }
        }
    }

    /**
     * The sub-command for unlocking an ability for a player.
     * @param player - Player running the command
     * @param targetPlayer - The target of the command
     */
    private void subComUnlock(EntityPlayer player, String ability, String targetPlayer){
        EntityPlayer otherPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(targetPlayer);
        if (player == null || process(player, otherPlayer != null, "That player doesn't exist!")) {
            SCPlayer ex = SCPlayer.getPlayer(otherPlayer);
            AbilityDatabase abilities = new AbilityDatabase();
            String[] attackList = abilities.getAllAttackNames();
            String[] passiveList = abilities.getAllPassiveNames();
            List<String> list = Arrays.asList(attackList);
            if (player != null && list.contains(ability)) {
                Attack unlockedAttack = abilities.getAttackByName(ability);
                if (unlockedAttack != null) {
                    ArrayList<Attack> playerAttacks = ex.getAttacks();
                    playerAttacks.add(unlockedAttack);
                    ex.setAttacks(playerAttacks);
                    System.out.println(playerAttacks);
                    player.addChatComponentMessage(new ChatComponentTranslation(
                            EnumChatFormatting.BLUE + targetPlayer + " has unlocked " + ability));
                }
            }
            List<String> list2 = Arrays.asList(passiveList);
            if (player != null && list2.contains(ability)){
                PassiveAbility unlockedPassive = abilities.getPassiveByName(ability);
                if (unlockedPassive != null){
                    ArrayList<PassiveAbility> playerPassives = ex.getPassives();
                    playerPassives.add(unlockedPassive);
                    ex.setPassives(playerPassives);
                    System.out.println(playerPassives);
                    player.addChatComponentMessage(new ChatComponentTranslation(
                            EnumChatFormatting.BLUE + targetPlayer + " has unlocked " + ability));
                }
            }
            if (player != null && !list.contains(ability) && !list2.contains(ability)){
                player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "Something went wrong. Ability names are case-sensitive and are capitalized, i.e. 'BigBangAttack'"));
            }
        }
    }

    /**
     * The sub-command for locking an ability from a player.
     * @param player - Player running the command
     * @param targetPlayer - The target of the command
     */
    private void subComLock(EntityPlayer player, String ability, String targetPlayer){
        EntityPlayer otherPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(targetPlayer);
        if (player == null || process(player, otherPlayer != null, "That player doesn't exist!")) {
            SCPlayer ex = SCPlayer.getPlayer(otherPlayer);
            AbilityDatabase abilities = new AbilityDatabase();
            String[] attackList = abilities.getAllAttackNames();
            String[] passiveList = abilities.getAllPassiveNames();
            List<String> list = Arrays.asList(attackList);
            List<String> list2 = Arrays.asList(passiveList);
            if (player != null && list.contains(ability)){
                ArrayList<Attack> playerAttacks = ex.getAttacks();
                ArrayList<Attack> newPlayerAttacks = new ArrayList<>();
                for (Attack attack : playerAttacks){
                    if (!attack.getName().equals(ability) || ability.equals("KiAttack") || ability.equals("EnergyWave")){
                        newPlayerAttacks.add(attack);
                    }
                }
                ex.setAttacks(newPlayerAttacks);
                System.out.println(newPlayerAttacks);
                if (ability.equals("KiAttack") || ability.equals("EnergyWave")){
                    player.addChatComponentMessage(new ChatComponentTranslation(
                            EnumChatFormatting.GOLD + "Default Abilities cannot be locked!"));
                } else {
                    player.addChatComponentMessage(new ChatComponentTranslation(
                            EnumChatFormatting.BLUE + ability + " has been locked for " + targetPlayer));
                }
            }
            if (player != null && list2.contains(ability)){
                ArrayList<PassiveAbility> playerPassives = ex.getPassives();
                ArrayList<PassiveAbility> newPlayerPassives = new ArrayList<>();
                for (PassiveAbility passive:playerPassives){
                    if (!passive.getName().equals(ability) || ability.equals("VirtuousSpirit")){
                        newPlayerPassives.add(passive);
                    }
                }
                ex.setPassives(newPlayerPassives);
                System.out.println(newPlayerPassives);
                if (ability.equals("VirtuousSpirit")){
                    player.addChatComponentMessage(new ChatComponentTranslation(
                            EnumChatFormatting.GOLD + "Default Abilities cannot be locked!"));
                } else {
                    player.addChatComponentMessage(new ChatComponentTranslation(
                            EnumChatFormatting.BLUE + ability + " has been locked for " + targetPlayer));
                }
            }
            if (player != null && !list.contains(ability) && !list2.contains(ability)){
                player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "Something went wrong. Ability names are case-sensitive and are capitalized, i.e. 'BigBangAttack'"));
            }
        }
    }

    /**
     * Checks whether the player is an operator of the server
     *
     * @param player The player in question
     * @return True if the player is an OP, false otherwise
     */
    private boolean isOp(EntityPlayer player) {
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
    }

    /**
     * Checks to see whether a sub-command can only be used by OPs
     *
     * @param s The sub-command being checked
     * @return True if the sub-command s can only be used by OPs, false otherwise
     */
    private boolean isOpCommand(String s) {
        String[] com = s.split(" ");
        return com[com.length - 1].equalsIgnoreCase("op");
    }

    /**
     * In-line method that allows the user to relay an error without requiring the
     * else clause
     *
     * @param player The player issuing the command
     * @param bool   The condition of whether an aspect of the command is valid
     * @param error  The specific error message that is displayed if bool is false
     * @return bool
     */
    private static boolean process(EntityPlayer player, boolean bool, String error) {

        if (!bool) {

            if (error.contains("&n")) {
                String[] msg = error.split(" &n ");

                for (String s : msg) {
                    player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.DARK_RED + s));
                }

            } else {
                player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.DARK_RED + error));
            }
        }

        return bool;
    }

    /**
     * @param sub
     * @return True if the commandlist contains the subcommand given
     */
    public boolean listContains(String sub) {
        for (String s : commandList) {
            if (s.split(" ")[0].equalsIgnoreCase(sub)) {
                return true;
            }
        }

        return false;
    }



}
