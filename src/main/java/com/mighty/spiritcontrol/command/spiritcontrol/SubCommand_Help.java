package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.command.SCSubCommand;
import net.minecraft.command.ICommandSender;
import somehussar.minimessage.MiniMessageParser;

import java.util.List;

public class SubCommand_Help extends SCSubCommand {
    String[] commandList = {
            "<aqua>help <dark_aqua>- shows a list of commands",
            "<aqua>skills <dark_aqua>- displays all unlocked skill names and descriptions",
            "<aqua>equip <slotName> <skillName> <dark_aqua>- equips the selected skill onto your loadout",
            "<aqua>enable [player] <dark_aqua>- unlocks Spirit Control for a Player OP",
            "<aqua>disable [player] <dark_aqua>- locks Spirit Control for a Player OP",
            "<aqua>check [player] <dark_aqua>- shows a Player's Gauge, loadout, and all unlocked abilities OP",
            "<aqua>unlock <skillName> [player] <dark_aqua>- unlocks an ability OP",
            "<aqua>lock <skillName> [player] <dark_aqua>- removes an ability OP" };

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        /**
         * @TODO
         * Change display
         */
        for (String s : commandList) {
            sender.addChatMessage(MiniMessageParser.getFormat(s));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
