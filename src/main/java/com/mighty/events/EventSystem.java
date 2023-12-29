package com.mighty.events;

import com.mighty.zsspiritcontrol.zsspiritcontrol;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import com.mighty.util.SCPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class EventSystem {
    /**
     * Registers a player for Spirit Control attributes.
     * @param event
     */
    @SubscribeEvent
    public void entityConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            SCPlayer.register((EntityPlayer)event.entity);
        }
    }

    /**
     * Adds Spirit to the Player's Gauge on attack.
     * @param event
     */
    @SubscribeEvent
    public void onEntityHit(LivingAttackEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            SCPlayer ex = SCPlayer.getPlayer(player);
            int gauge = ex.getCurrGauge();
            int cap = ex.getGaugeCapacity();
            if (gauge < cap) {
                gauge = gauge + 1;
                ex.setCurrGauge(gauge);
            }
        }
    }
}
