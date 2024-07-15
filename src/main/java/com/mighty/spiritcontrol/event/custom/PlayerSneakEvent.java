package com.mighty.spiritcontrol.event.custom;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerSneakEvent extends Event {

    public final EntityPlayer player;
    public final boolean sneaked;
    public PlayerSneakEvent(EntityPlayer player, boolean isSneaking){
        this.player = player;
        this.sneaked = isSneaking;
    }
}
