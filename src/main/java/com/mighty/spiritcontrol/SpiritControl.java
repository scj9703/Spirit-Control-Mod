package com.mighty.spiritcontrol;

import com.mighty.spiritcontrol.player.SCPlayer;
import com.mighty.spiritcontrol.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

@Mod(modid = SpiritControl.MODID, version = SpiritControl.VERSION, name="Spirit Control Plugin", useMetadata = true)
public class SpiritControl
{
    public static final String MODID = "spiritcontrol";
    public static final String VERSION = "1.0.0";

    public static Logger LOGGER;

    @Mod.Instance
    public static SpiritControl INSTANCE;

    @SidedProxy(clientSide = "com.mighty.spiritcontrol.proxy.CommonProxy", serverSide = "com.mighty.spiritcontrol.proxy.CommonProxy")
    public static CommonProxy proxy;

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

    /**
     * Reloads all player SC data.
     * Used for reloading ability configs.
     *
     * @reason: Abilities are created as new instances and players need their references to it updated. Config reloads are not supposed to happen very often.
     */
    public static void reloadPlayerData(){
        for(Object plrObject : MinecraftServer.getServer().getConfigurationManager().playerEntityList){
            if(!(plrObject instanceof EntityPlayer))
                continue;

            SCPlayer player = SCPlayer.getPlayer((EntityPlayer) plrObject);
            player.copy(player);
        }
    }

}
