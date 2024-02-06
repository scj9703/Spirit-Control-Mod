package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import somehussar.minimessage.MMParser;
import tv.twitch.chat.Chat;

import java.util.*;

public class SubCommand_Help extends SCSubCommand {
    public SubCommand_Help(SCCommandBase parent) {
        super(parent);
    }
//    String[] commandList = {
//            "<aqua>help <dark_aqua>- shows a list of commands",
//            "<aqua>skills <dark_aqua>- displays all unlocked skill names and descriptions",
//            "<aqua>equip <slotName> <skillName> <dark_aqua>- equips the selected skill onto your loadout",
//            "<aqua>enable [player] <dark_aqua>- unlocks Spirit Control for a Player OP",
//            "<aqua>disable [player] <dark_aqua>- locks Spirit Control for a Player OP",
//            "<aqua>check [player] <dark_aqua>- shows a Player's Gauge, loadout, and all unlocked abilities OP",
//            "<aqua>unlock <skillName> [player] <dark_aqua>- unlocks an ability OP",
//            "<aqua>lock <skillName> [player] <dark_aqua>- removes an ability OP" };

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        sender.addChatMessage(MMParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
        if(args.length == 1){
            sender.addChatMessage(MMParser.getFormat("<aqua>Triple-Shift <dark_aqua>to enable/disable your Abilities!"));
            sender.addChatMessage(MMParser.getFormat("<aqua>Shift + Punch <dark_aqua>in the <green>1st/2nd/3rd <dark_aqua>hotbar slots to select your attack!"));
            sender.addChatMessage(MMParser.getFormat("<aqua>Shift + Hold C <dark_aqua>to charge your attack!"));
            sender.addChatMessage(new ChatComponentText(""));
            sender.addChatMessage(MMParser.getFormat("<dark_aqua>For help with commands run <aqua>/"+parent.getCommandName() + " help commands"));
        }

        if(args.length > 1 && args[1].equalsIgnoreCase("commands")) {

            for (Map.Entry<String, SCSubCommand> cmdEntry : parent.getSubCommands().entrySet()) {
                SCSubCommand cmd = cmdEntry.getValue();
                if (cmd.hasPerms(sender))
                    sender.addChatMessage(MMParser.getFormat(cmd.getHelpMessage()));
            }

        }
        sender.addChatMessage(MMParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2)
            return Collections.singletonList("commands");
        return null;
    }

    @Override
    public String getHelpMessage() {
        return "<aqua>/"+parent.getCommandName()+" help <dark_aqua>- shows helpful information";
    }
}
