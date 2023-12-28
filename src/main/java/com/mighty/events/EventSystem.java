package com.mighty.events;

import com.mighty.zsspiritcontrol.zsspiritcontrol;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import com.mighty.util.SCPlayer;

public class EventSystem {
    @SubscribeEvent
    public void entityConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            SCPlayer.register((EntityPlayer)event.entity);
        }
    }
}
