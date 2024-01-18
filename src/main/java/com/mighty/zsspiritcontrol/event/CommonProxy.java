package com.mighty.zsspiritcontrol.event;

import com.mighty.zsspiritcontrol.command.spiritcontrol.Command_SpiritControl;
import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpiritControlEventHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new SpiritControlEventHandler());
        MinecraftForge.ORE_GEN_BUS.register(new SpiritControlEventHandler());
        FMLCommonHandler.instance().bus().register(new SpiritControlEventHandler());
    }

    public void fmlLifeCycleEvent(FMLPostInitializationEvent event) {
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