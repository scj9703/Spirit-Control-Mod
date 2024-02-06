package com.mighty.spiritcontrol.command.spiritcontrol;

import com.mighty.spiritcontrol.command.SCCommandBase;
import com.mighty.spiritcontrol.command.SCSubCommand;
import com.mighty.spiritcontrol.player.SCPlayer;
import com.mighty.spiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import somehussar.minimessage.MMParser;
import somehussar.minimessage.util.Util;

import java.util.Arrays;
import java.util.List;

public class Command_SpiritControl extends SCCommandBase {

    public static SCCommandBase INSTANCE;
    public Command_SpiritControl(){
        INSTANCE = this;
        subCommandMap.put("help", new SubCommand_Help(this));
        subCommandMap.put("skills", new SubCommand_Skills(this));
        subCommandMap.put("equip", new SubCommand_Equip(this));
        subCommandMap.put("enable", new SubCommand_Enable(this).addPerms(EnumPermission.SPIRITCONTROL_ENABLE));
        subCommandMap.put("disable", new SubCommand_Disable(this).addPerms(EnumPermission.SPIRITCONTROL_DISABLE));
        subCommandMap.put("check", new SubCommand_Check(this).addPerms(EnumPermission.SPIRITCONTROL_CHECK));
        subCommandMap.put("unlock", new SubCommand_Unlock(this).addPerms(EnumPermission.SPIRITCONTROL_LOCK));
        subCommandMap.put("lock", new SubCommand_Lock(this).addPerms(EnumPermission.SPIRITCONTROL_UNLOCK));
    }
    @Override
    public String getCommandName() {
        return "spc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/spc help";
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList("spc", "spiritcontrol");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 0){
            if(sender instanceof EntityPlayerMP){
                SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) sender);

                if(!extPlayer.hasUnlockedSpiritControl()){
                    sender.addChatMessage(MMParser.getFormat("<dark_aqua>You haven't learned how to use Spirit Control! Seek training on <dark_purple><bold>Yardrat!"));
                    return;
                }

                /**
                 * @TODO
                 * Write out this command better
                 */

                IChatComponent prettyGauge = extPlayer.drawPrettyGauge();

                sender.addChatMessage(MMParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> Your Spirit Control Loadout"));
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(prettyGauge);
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> <yellow>Super Attack 1: <underline><aqua><ability></underline>", "ability", Util.getAbilityHover(extPlayer.getAbilityFromSlot("super1"))));
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> <yellow>Super Attack 2: <underline><aqua><ability></underline>", "ability", Util.getAbilityHover(extPlayer.getAbilityFromSlot("super2"))));
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> <gold>Ultimate Attack: <underline><aqua><ability></underline>", "ability", Util.getAbilityHover(extPlayer.getAbilityFromSlot("ultimate"))));
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> <blue>Passive Ability: <underline><aqua><ability></underline>", "ability", Util.getAbilityHover(extPlayer.getAbilityFromSlot("passive"))));
                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> Do <aqua>/<cmd_name> help</aqua> for help!", "cmd_name", getCommandName()));

                sender.addChatMessage(MMParser.getFormat("<dark_aqua>>"));
                sender.addChatMessage(MMParser.getFormat("<dark_gray>{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}"));
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
