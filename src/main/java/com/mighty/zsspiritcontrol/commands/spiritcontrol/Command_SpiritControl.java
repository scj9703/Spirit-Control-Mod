package com.mighty.zsspiritcontrol.commands.spiritcontrol;

import com.mighty.zsspiritcontrol.commands.SCCommandBase;
import com.mighty.zsspiritcontrol.commands.SCSubCommand;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import somehussar.minimessage.MiniMessageParser;
import somehussar.minimessage.util.Util;

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
        return "sc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sc";
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
                 * @TODO
                 * Write out this command better
                 */

                IChatComponent prettyGauge = extPlayer.drawPrettyGauge();

                sender.addChatMessage(MiniMessageParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> Your Spirit Control Loadout"));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(prettyGauge);
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <yellow>Super Attack 1: <underline><aqua>" + Util.getAbilityHoverValue(extPlayer.getAbilityFromSlot("super1"))));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <yellow>Super Attack 2: <underline><aqua>" + Util.getAbilityHoverValue(extPlayer.getAbilityFromSlot("super2"))));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <gold>Ultimate Attack: <underline><aqua>" + Util.getAbilityHoverValue(extPlayer.getAbilityFromSlot("ultimate"))));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> <blue>Passive Ability: <underline><aqua>" + Util.getAbilityHoverValue(extPlayer.getAbilityFromSlot("passive"))));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> Do <aqua>/sc help</aqua> for a full list of commands!"));
                sender.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>>"));
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
