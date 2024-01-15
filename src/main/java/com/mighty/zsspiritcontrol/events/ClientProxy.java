package com.mighty.zsspiritcontrol.events;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        super.fmlLifeCycleEvent(event);

    }

    @Override
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        super.fmlLifeCycleEvent(event);
    }

    @Override
    public void fmlLifeCycleEvent(FMLPostInitializationEvent event) {
        super.fmlLifeCycleEvent(event);
    }

}