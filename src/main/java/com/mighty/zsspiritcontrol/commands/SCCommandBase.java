package com.mighty.zsspiritcontrol.commands;

import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SCCommandBase extends CommandBase {
    protected final HashMap<String, SCSubCommand> subCommandMap = new HashMap<>();

    protected final ArrayList<BukkitWrapper.Permission> permsList = new ArrayList<>();

    protected String[] filterCommandsByPerms(ICommandSender sender) {
        ArrayList<String> filteredCommands = new ArrayList<>();

        for(String name : subCommandMap.keySet()){
            SCSubCommand cmd = subCommandMap.get(name);
            if(cmd.hasPerms(sender))
                filteredCommands.add(name);
        }

        return filteredCommands.toArray(new String[0]);
    }

    public static String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean hasPerms(ICommandSender sender){
        if(!(sender instanceof EntityPlayerMP))
            return true;

        for(BukkitWrapper.Permission permNode : permsList){
            if(BukkitWrapper.hasPermission((EntityPlayer) sender, permNode))
                return true;
        }
        return permsList.isEmpty();
    }

    public SCCommandBase addPerms(Object... perms){
        for(Object permNode : perms){
            if(permNode instanceof EnumPermission){
                permsList.add(((EnumPermission) permNode).permNode);
            }else if(permNode instanceof BukkitWrapper.Permission){
                permsList.add((BukkitWrapper.Permission) permNode);
            }else{
                throw new IllegalArgumentException("Expected arguments of type EnumPermission or BukkitWrapper.Permission");
            }
        }

        return this;
    }
}
