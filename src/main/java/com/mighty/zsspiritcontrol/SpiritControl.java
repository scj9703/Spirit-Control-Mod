package com.mighty.zsspiritcontrol;

import com.mighty.zsspiritcontrol.config.Config;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import com.mighty.zsspiritcontrol.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = SpiritControl.MODID, version = SpiritControl.VERSION, name="Spirit Control Plugin", useMetadata = true)
public class SpiritControl
{
    public static final String MODID = "ZSSpiritcontrol";
    public static final String VERSION = "1.0.0";

    public static Config CONFIG;
    public static Logger LOGGER;

    @Mod.Instance
    public static SpiritControl INSTANCE;


    @SidedProxy(clientSide = "com.mighty.zsspiritcontrol.proxy.ClientProxy", serverSide = "com.mighty.zsspiritcontrol.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static File getModFile(){
        return new File(FMLCommonHandler.instance().findContainerFor(SpiritControl.INSTANCE).getSource().toString());
    }

    @EventHandler
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {


        proxy.fmlLifeCycleEvent(event);
    }

    @EventHandler
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {

        proxy.fmlLifeCycleEvent(event);
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

    public static void reloadPlayerData(){
        for(Object plrObject : MinecraftServer.getServer().getConfigurationManager().playerEntityList){
            if(!(plrObject instanceof EntityPlayer))
                continue;

            SCPlayer player = SCPlayer.getPlayer((EntityPlayer) plrObject);
            player.copy(player);
        }
    }

}
