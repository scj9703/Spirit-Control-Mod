package com.mighty.spiritcontrol.event.custom;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.Event;

public class PlayerSwingEvent extends Event {

    public final EntityPlayer player;
    public PlayerSwingEvent(EntityPlayer entityPlayer) {
        this.player = entityPlayer;
    }
}
