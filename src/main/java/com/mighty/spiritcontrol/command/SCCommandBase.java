package com.mighty.spiritcontrol.command;

import com.mighty.spiritcontrol.player.permission.BukkitWrapper;
import com.mighty.spiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.collections4.OrderedMap;

import java.util.*;

public abstract class SCCommandBase extends CommandBase {
    protected final Map<String, SCSubCommand> subCommandMap = new LinkedHashMap<>();

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
        //If the command sender is not a player, return true
        if(!(sender instanceof EntityPlayerMP))
            return true;

        //Check if the player has at least one of the permissions required to run this command
        for(BukkitWrapper.Permission permNode : permsList){
            if(BukkitWrapper.hasPermission((EntityPlayer) sender, permNode))
                return true;
        }
        return permsList.isEmpty();
    }

    /**
     * Registers perms to the command.
     * @param perms List of permissions to register
     * @return Reference to the command.
     */
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

    public Map<String, SCSubCommand> getSubCommands(){
        return subCommandMap;
    }
}
