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
                /**
                 * @TODO:
                 * Write out player skills
                 */
            }else{
                /**
                 * @TODO:
                 * Add chat message that you have to be a player.
                 */
            }
        } else {
            SubCommand subCommand = subCommandMap.getOrDefault(args[0], null);
            if(subCommand != null)
                subCommand.processCommand(sender, args);
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
