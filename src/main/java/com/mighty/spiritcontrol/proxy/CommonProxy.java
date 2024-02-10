package com.mighty.spiritcontrol.proxy;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.api.SPCApi;
import com.mighty.spiritcontrol.command.configreload.Command_SCReload;
import com.mighty.spiritcontrol.command.spiritcontrol.Command_SpiritControl;
import com.mighty.spiritcontrol.config.Config;
import com.mighty.spiritcontrol.event.SpiritControlHandler;
import com.mighty.spiritcontrol.player.permission.BukkitWrapper;
import com.mighty.spiritcontrol.player.permission.EnumPermission;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.scripted.NpcAPI;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        SpiritControl.LOGGER = event.getModLog();

        new Config(event.getModConfigurationDirectory());

        //Loads the class early so permissions are properly registered before listing them out
        EnumPermission.init();
    }

    public void init(FMLInitializationEvent event) {
        //Attempts to find Bukkit and list out all permissions
        BukkitWrapper.init();

        SpiritControlHandler eventHandler = new SpiritControlHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
//        MinecraftForge.TERRAIN_GEN_BUS.register(eventHandler);
//        MinecraftForge.ORE_GEN_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);

        NpcAPI.engineObjects.put("SPCApi", new SPCApi());
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new Command_SpiritControl());
        event.registerServerCommand(new Command_SCReload().addPerms(EnumPermission.SPIRITCONTROL_RELOAD));
    }

}