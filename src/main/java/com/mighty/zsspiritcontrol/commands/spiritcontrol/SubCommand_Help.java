package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SCSubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class SubCommand_Help extends SCSubCommand {
    String[] commandList = {
            "help - shows a list of commands",
            "skills - displays all unlocked skill names and descriptions",
            "equip <slotName> <skillName> - equips the selected skill onto your loadout",
            "enable [player] - unlocks Spirit Control for a Player OP",
            "disable [player] - locks Spirit Control for a Player OP",
            "check [player] - shows a Player's Gauge, loadout, and all unlocked abilities OP",
            "unlock <skillName> [player] - unlocks an ability OP",
            "lock <skillName> [player] - removes an ability OP" };

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        /**
         * @TODO
         * Change display
         */
        for (String s : commandList) {
            sender.addChatMessage((new ChatComponentTranslation(s).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA))));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
