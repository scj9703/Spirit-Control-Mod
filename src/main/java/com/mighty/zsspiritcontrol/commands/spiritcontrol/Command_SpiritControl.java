package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Command_SpiritControl extends CommandBase {

    private final HashMap<String, SubCommand> subCommandMap = new HashMap<>();

    private final ArrayList<BukkitWrapper.Permission> permsList = new ArrayList<>();

    public Command_SpiritControl(Object... perms){
        this();

        for(Object permNode : perms){
            if(permNode instanceof EnumPermission){
                permsList.add(((EnumPermission) permNode).permNode);
            }else if(permNode instanceof BukkitWrapper.Permission){
                permsList.add((BukkitWrapper.Permission) permNode);
            }else{
                throw new IllegalArgumentException("Expected arguments of type EnumPermission or BukkitWrapper.Permission");
            }
        }
    }
    public Command_SpiritControl(){
//        subCommandMap.put("help", new SubCommand_Help());
//        subCommandMap.put("skills", new SubCommand_Skills());
//        subCommandMap.put("enable", new SubCommand_Enable().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_ENABLE));
//        subCommandMap.put("disable", new SubCommand_Disable().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_DISABLE));
//        subCommandMap.put("check", new SubCommand_Check().addPerms(EnumPermission.SPIRITCONTROL_CHECK));
//        subCommandMap.put("unlock", new SubCommand_Unlock().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_LOCK));
//        subCommandMap.put("lock", new SubCommand_Lock().addPerms(EnumPermission.SPIRITCONTROL_TOGGLE, EnumPermission.SPIRITCONTROL_UNLOCK));
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
        return Arrays.asList("newsc", "spiritcontrol");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0){
            if(sender instanceof EntityPlayerMP){
                SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

                if(!extPlayer.isEnabled()){
                    sender.addChatMessage(new ChatComponentText("You haven't learned how to use Spirit Control! Seek training on \u00a75\u00a7lYardrat!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                    return;
                }
                /**
                 * @TODO:
                 * Write out player skills (better than this)
                 * for the record. I hate using ChatComponents like this T-T+
                 */
                double gauge = extPlayer.getCurrGauge();
                double cap = extPlayer.getGaugeCapacity();
                double percent = (gauge / cap) * 100; // For gauge display
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedPercent = decimalFormat.format(percent);
                sender.addChatMessage(new ChatComponentTranslation("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> Your Spirit Control Loadout").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> " + extPlayer.printGauge() + " Your Spirit Gauge is at " + formattedPercent + " Percent Capacity.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> Super Attack 1: " + extPlayer.getSuperAttack1().getDesc()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> Super Attack 2: " + extPlayer.getSuperAttack2().getDesc()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> Ultimate Attack: " + extPlayer.getUltimateAttack().getDesc()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> Passive Ability: " + extPlayer.getPassiveAbility().getDesc()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation(">").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("==> Do /sc help for a full list of commands!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
                sender.addChatMessage(new ChatComponentTranslation("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
            }else{
                sender.addChatMessage((IChatComponent) new ChatComponentText("You have to be a player to run this command.").getChatStyle().setColor(EnumChatFormatting.RED));
            }
        } else {
            SubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
            if(subCommand != null)
                subCommand.processCommand(sender, args);
            else
                sender.addChatMessage((IChatComponent) new ChatComponentText("A subcommand by this name doesn't exist.").getChatStyle().setColor(EnumChatFormatting.RED));
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
        SubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
        return subCommand != null ? subCommand.addTabCompletionOptions(sender, args) : null;
    }

    private String[] filterCommandsByPerms(ICommandSender sender) {
        ArrayList<String> filteredCommands = new ArrayList<>();

        for(String name : subCommandMap.keySet()){
            SubCommand cmd = subCommandMap.get(name);
            if(cmd.hasPerms(sender))
                filteredCommands.add(name);
        }

        return filteredCommands.toArray(new String[0]);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if(args[0].isEmpty()){
            return false;
        }
        SubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
        return subCommand != null && subCommand.isUsernameIndex(args, index);
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    protected boolean hasPerms(EntityPlayer player){
        for(BukkitWrapper.Permission permNode : permsList){
            if(BukkitWrapper.hasPermission(player, permNode))
                return true;
        }
        return permsList.isEmpty();
    }


}
