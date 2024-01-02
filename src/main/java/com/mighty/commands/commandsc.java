package com.mighty.commands;
import java.text.DecimalFormat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import com.mighty.util.SCPlayer;

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
