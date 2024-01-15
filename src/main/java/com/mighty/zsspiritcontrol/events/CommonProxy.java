package com.mighty.zsspiritcontrol.events;

import com.mighty.zsspiritcontrol.commands.commandsc;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventSystem());
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventSystem());
        MinecraftForge.ORE_GEN_BUS.register(new EventSystem());
        FMLCommonHandler.instance().bus().register(new EventSystem());
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
        event.registerServerCommand(new commandsc());
    }

}