package com.mighty.zsspiritcontrol.commands;

import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    private final List<BukkitWrapper.Permission> permsList = new ArrayList<>();

    public abstract void processCommand(ICommandSender sender, String[] args);


    public abstract List<String> addTabCompletionOptions(ICommandSender sender, String[] args);

    public abstract boolean isUsernameIndex(String[] args, int index);

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public static List<String> getListOfStringMatchingLastWord(String[] args, String... possibleMatches){
        return CommandBase.getListOfStringsMatchingLastWord(args, possibleMatches);
    }

    public static EntityPlayerMP getPlayer(ICommandSender sender, String name){
        return CommandBase.getPlayer(sender, name);
    }

    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender){
        return CommandBase.getCommandSenderAsPlayer(sender);
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

    public SubCommand addPerms(Object... perms){
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
