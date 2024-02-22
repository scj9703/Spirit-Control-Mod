package com.mighty.spiritcontrol;

import com.mighty.spiritcontrol.player.SCPlayer;
import com.mighty.spiritcontrol.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

@Mod(modid = SpiritControl.MODID, version = SpiritControl.VERSION, name="Spirit Control Plugin", acceptableRemoteVersions = "*",  useMetadata = true)
public class SpiritControl
{
    public static final String MODID = "spiritcontrol";
    public static final String VERSION = "1.0.12";

    public static Logger LOGGER;

    @Mod.Instance
    public static SpiritControl INSTANCE;

    @SidedProxy(clientSide = "com.mighty.spiritcontrol.proxy.CommonProxy", serverSide = "com.mighty.spiritcontrol.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    /**
     * Reloads all player SC data.
     * Used for reloading ability configs.
     *
     * reason: Abilities are created as new instances and players need their references to it updated. Config reloads are not supposed to happen very often.
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
