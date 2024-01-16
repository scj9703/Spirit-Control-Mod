package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SCCommandBase;
import com.mighty.zsspiritcontrol.commands.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;

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
                    sender.addChatMessage(ChatUtil.getMessage("You haven't learned how to use Spirit Control! Seek training on \u00a75\u00a7lYardrat!", EnumChatFormatting.AQUA));
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
                sender.addChatMessage(ChatUtil.getMessage("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> Your Spirit Control Loadout", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> " + extPlayer.drawSpiritGauge() + " Your Spirit Gauge is at " + formattedPercent + " Percent Capacity.", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> Super Attack 1: " + extPlayer.getAbilityFromSlot("super1").getDescription(), EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> Super Attack 2: " + extPlayer.getAbilityFromSlot("super2").getDescription(), EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> Ultimate Attack: " + extPlayer.getAbilityFromSlot("ultimate").getDescription(), EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> Passive Ability: " + extPlayer.getAbilityFromSlot("passive").getDescription(), EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage(">", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("==> Do /sc help for a full list of commands!", EnumChatFormatting.AQUA));
                sender.addChatMessage(ChatUtil.getMessage("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}", EnumChatFormatting.AQUA));
            }else{
                sender.addChatMessage(ChatUtil.getMessage("You have to be a player to run this command.", EnumChatFormatting.RED));
            }
        } else {
            SCSubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
            if(subCommand != null)
                subCommand.processCommand(sender, args);
            else
                sender.addChatMessage(ChatUtil.getMessage("A subcommand by this name doesn't exist.", EnumChatFormatting.RED));
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
