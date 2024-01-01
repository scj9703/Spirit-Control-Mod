package com.mighty.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import com.mighty.util.SCPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

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
     * Adds Spirit to the Player's Gauge on attack or when hit.
     * @param event - Event when a living entity is attacked.
     */
    @SubscribeEvent
    public void onEntityHit(LivingAttackEvent event) {
        // If the player is the CAUSE of the attack
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            SCPlayer ex = SCPlayer.getPlayer(player);
            double gauge = ex.getCurrGauge();
            double cap = ex.getGaugeCapacity();
            if (gauge < cap) {
                gauge = gauge + 1;
                ex.setCurrGauge(gauge);
            }
        }

        // If the player is the RECIPIENT of the attack
        if (event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entity;
            SCPlayer ex = SCPlayer.getPlayer(player);
            double gauge = ex.getCurrGauge();
            double cap = ex.getGaugeCapacity();
            if (gauge < cap) {
                gauge = gauge + 1;
                ex.setCurrGauge(gauge);
            }
        }
    }

    /**
     * Fills Spirit Gauge on update. Used in a few passive skills only.
     * @param event - Entity update event.
     */
    @SubscribeEvent
    public void onEntityTick(LivingUpdateEvent event){
        if (event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entity;
            SCPlayer ex = SCPlayer.getPlayer(player);
            double gauge = ex.getCurrGauge();
            double cap = ex.getGaugeCapacity();
            if (gauge < cap) {
                // gauge = gauge + 0.01; Un-Comment this to test it.
                // Otherwise, leave it commented out until passives work.
                // ex.setCurrGauge(gauge);
            }
        }
    }
}
