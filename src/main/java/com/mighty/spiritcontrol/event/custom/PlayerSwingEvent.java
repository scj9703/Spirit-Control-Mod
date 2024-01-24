package com.mighty.spiritcontrol.event.custom;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerSwingEvent extends Event {

    public final EntityPlayer player;
    public PlayerSwingEvent(EntityPlayer entityPlayer) {
        this.player = entityPlayer;
    }
}
