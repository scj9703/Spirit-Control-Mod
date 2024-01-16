package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SCCommandBase;
import com.mighty.zsspiritcontrol.commands.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;
import somehussar.minimessage.MiniMessageParser;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Command_SpiritControl extends SCCommandBase {
    public Command_SpiritControl(){
        subCommandMap.put("help", new SubCommand_Help());
        subCommandMap.put("skills", new SubCommand_Skills());
        subCommandMap.put("equip", new SubCommand_Equip());
        subCommandMap.put("enable", new SubCommand_Enable().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_ENABLE));
        subCommandMap.put("disable", new SubCommand_Disable().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_DISABLE));
        subCommandMap.put("check", new SubCommand_Check().addPerms(EnumPermission.SPIRITCONTROL_CHECK));
        subCommandMap.put("unlock", new SubCommand_Unlock().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_LOCK));
        subCommandMap.put("lock", new SubCommand_Lock().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_UNLOCK));
    }
    @Override
    public String getCommandName() {
        return "spiritcontrol";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/spiritcontrol";
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("sc", "spiritcontrol");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0){
            if(sender instanceof EntityPlayerMP){
                SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

                if(!extPlayer.isEnabled()){
                    sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>You haven't learned how to use Spirit Control! Seek training on <dark_purple><bold>Yardrat!"));
                    return;
                }
                /**
                 * @TODO:
                 * Write out player skills (better than this)
                 * for the record. I hate using ChatComponents like this T-T+
                 */
                double gauge = extPlayer.getSpirit();
                double cap = extPlayer.getMaxSpirit();
                double percent = (gauge / cap) * 100; // For gauge display
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedPercent = decimalFormat.format(percent);
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> Your Spirit Control Loadout"));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <gray><gauge></gray> Your spirit gauge is at <gray><percent>%</gray> capacity.", "gauge", extPlayer.drawSpiritGauge(), "percent", formattedPercent));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <yellow>Super Attack 1: <gray>" + extPlayer.getAbilityFromSlot("super1").getDescription()));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <yellow>Super Attack 2: <gray>" + extPlayer.getAbilityFromSlot("super2").getDescription()));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <gold>Ultimate Attack: <gray>" + extPlayer.getAbilityFromSlot("ultimate").getDescription()));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <blue>Passive Ability: <gray>" + extPlayer.getAbilityFromSlot("passive").getDescription()));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <gray>Do <aqua>/sc help</aqua> for a full list of commands!"));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
            }else{
                throw new WrongUsageException("You have to be a player to use this command.");
            }
        } else {
            SCSubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
            if(subCommand != null)
                subCommand.processCommand(sender, args);
            else
                throw new WrongUsageException("A subcommand by this name doesn't exist.");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 1){
            return getListOfStringsMatchingLastWord(args, filterCommandsByPerms(sender));
        }
        SCSubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
        return subCommand != null ? subCommand.addTabCompletionOptions(sender, args) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if(args[0].isEmpty()){
            return false;
        }
        SCSubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
        return subCommand != null && subCommand.isUsernameIndex(args, index);
    }



}
