package com.mighty.zsspiritcontrol.proxy;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.command.abilityreload.Command_SCReload;
import com.mighty.zsspiritcontrol.command.spiritcontrol.Command_SpiritControl;
import com.mighty.zsspiritcontrol.config.Config;
import com.mighty.zsspiritcontrol.event.PlayerEventHandler;
import com.mighty.zsspiritcontrol.player.permission.BukkitWrapper;
import com.mighty.zsspiritcontrol.player.permission.EnumPermission;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {

        SpiritControl.LOGGER = event.getModLog();

        SpiritControl.CONFIG = new Config(event.getModConfigurationDirectory());
        SpiritControl.CONFIG.loadAbilities();

        //Loads the class early so permissions are properly registered before listing them out
        EnumPermission.init();
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        //Attempts to find Bukkit and list out all permissions
        BukkitWrapper.init();

        PlayerEventHandler eventHandler = new PlayerEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
//        MinecraftForge.TERRAIN_GEN_BUS.register(eventHandler);
//        MinecraftForge.ORE_GEN_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);
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
        event.registerServerCommand(new Command_SCReload().addPerms(EnumPermission.SPIRITCONTROL_RELOAD));
    }

}