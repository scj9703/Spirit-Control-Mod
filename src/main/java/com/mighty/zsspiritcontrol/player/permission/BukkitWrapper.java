package com.mighty.zsspiritcontrol.player.permission;

import com.mighty.zsspiritcontrol.SpiritControl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class BukkitWrapper {
    private static Method getPlayer;
    private static Method hasBukkitPerm;

    private static boolean isLoaded = false;

    public static void init(){
        try{
            getPlayer = Class.forName("org.bukkit.Bukkit").getMethod("getPlayer", String.class);
            hasBukkitPerm = Class.forName("org.bukkit.entity.Player").getMethod("hasPermission", String.class);

            isLoaded = true;

            Permission.permList.sort(String.CASE_INSENSITIVE_ORDER);

            SpiritControl.LOGGER.info("Bukkit permissions available: ");
            for(String p : Permission.permList){
                SpiritControl.LOGGER.info(p);
            }

        }catch(Exception e){
            SpiritControl.LOGGER.warn("Error, Bukkit permissions unavailable. Cause: ", e);
        }
    }

    public static boolean hasPermission(EntityPlayer player, EnumPermission permission){
        return hasPermission(player, permission.permNode);
    }

    public static boolean hasPermission(EntityPlayer player, Permission permission){
        return hasPermission(player, permission.name, permission.isOp);
    }

    public static boolean hasPermission(EntityPlayer player, String permission){
        return hasPermission(player, permission, false);
    }

    public static boolean hasPermission(EntityPlayer player, String permission, boolean isOpPermission){
        if(isLoaded){
            try {
                Object bukkitPlayer = getPlayer.invoke(null, player.getCommandSenderName());
                return (boolean) hasBukkitPerm.invoke(bukkitPlayer, permission);
            } catch(Exception e){
                SpiritControl.LOGGER.error(e);
            }
        }

        //Fallback in the case that Bukkit failed to load.
        //If the command is supposed to be an OP command and the player isn't OP'd
        return !isOpPermission || isPlayerOp(player);
    }

    private static boolean isPlayerOp(EntityPlayer player){
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
    }

    public static class Permission{
        private static ArrayList<String> permList = new ArrayList<>();
        public String name;
        public boolean isOp = true;

        public Permission(String permNode){
            this.name = permNode;
            if(!permList.contains(permNode))
                permList.add(permNode);
        }
        public Permission(String permNode, boolean op){
            this(permNode);
            this.isOp = op;
        }
    }
}
