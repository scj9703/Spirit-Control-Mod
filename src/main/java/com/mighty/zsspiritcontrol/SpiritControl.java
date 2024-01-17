package com.mighty.zsspiritcontrol;

import com.mighty.zsspiritcontrol.event.CommonProxy;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.SidedProxy;
import org.apache.logging.log4j.Logger;

@Mod(modid = SpiritControl.MODID, version = SpiritControl.VERSION, name="Spirit Control Plugin", useMetadata = true)
public class SpiritControl
{
    public static final String MODID = "ZSSpiritcontrol";
    public static final String VERSION = "1.0.0";

    public static Logger LOGGER;

    @SidedProxy(clientSide = "com.mighty.zsspiritcontrol.event.ClientProxy", serverSide = "com.mighty.zsspiritcontrol.event.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        proxy.fmlLifeCycleEvent(event);
        // some example code
        System.out.println("DIRT BLOCK2 >> "+Blocks.dirt.getUnlocalizedName());
    }

    @EventHandler
    public void fmlLifeCycle(FMLPostInitializationEvent event) {
        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycle(FMLServerAboutToStartEvent event) {
        proxy.fmlLifeCycleEvent(event);

    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStartingEvent event) {
        proxy.fmlLifeCycleEvent(event);

    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStartedEvent event) {
        proxy.fmlLifeCycleEvent(event);

    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStoppingEvent event) {
        proxy.fmlLifeCycleEvent(event);

    }

    @EventHandler
    public void fmlLifeCycle(FMLServerStoppedEvent event) {
        proxy.fmlLifeCycleEvent(event);

    }

}
