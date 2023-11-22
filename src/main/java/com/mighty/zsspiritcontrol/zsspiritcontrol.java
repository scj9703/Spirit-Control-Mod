package com.mighty.zsspiritcontrol;


import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = zsspiritcontrol.MODID, version = zsspiritcontrol.VERSION)
public class zsspiritcontrol
{
    public static final String MODID = "ZSSpiritcontrol";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
