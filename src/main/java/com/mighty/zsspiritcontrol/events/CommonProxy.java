package com.mighty.zsspiritcontrol.events;

import com.mighty.zsspiritcontrol.commands.spiritcontrol.Command_SpiritControl;
import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        //Loads the class so permissions are properly registered before listing them out
        EnumPermission.init();
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventSystem());
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventSystem());
        MinecraftForge.ORE_GEN_BUS.register(new EventSystem());
        FMLCommonHandler.instance().bus().register(new EventSystem());
    }

    public void fmlLifeCycleEvent(FMLPostInitializationEvent event) {
        //Attempts to find Bukkit and list out all permissions
        BukkitWrapper.init();
    }

    public void fmlLifeCycleEvent(FMLServerAboutToStartEvent event) {
    }

    public void fmlLifeCycleEvent(FMLServerStartedEvent event) {

    }

    public void fmlLifeCycleEvent(FMLServerStoppingEvent event) {
    }

    public void fmlLifeCycleEvent(FMLServerStoppedEvent event) {

    }

    public void fmlLifeCycleEvent(FMLServerStartingEvent event) {
        event.registerServerCommand(new Command_SpiritControl());
    }

}