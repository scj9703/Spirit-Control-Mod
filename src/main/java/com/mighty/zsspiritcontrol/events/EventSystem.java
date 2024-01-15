package com.mighty.zsspiritcontrol.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventSystem {
    /**
     * Registers a player for Spirit Control attributes.
     * @param event
     */
    @SubscribeEvent
    public void entityConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && SCPlayer.getPlayer((EntityPlayer) event.entity) == null) {
            SCPlayer.register((EntityPlayer)event.entity);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        SCPlayer.getPlayer(event.entityPlayer).copy(SCPlayer.getPlayer(event.original));
    }

    /**
     * Adds Spirit to the Player's Gauge on attack or when hit.
     * @param event - Event when a living entity is attacked.
     */
    @SubscribeEvent
    public void onEntityHit(LivingAttackEvent event) {
        if(event.entity.worldObj.isRemote){ //Return if even ran on client
            return;
        }

        // If the player is the CAUSE of the attack
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            SCPlayer ex = SCPlayer.getPlayer(player);
            boolean hasUnlocked = ex.isEnabled();
            if (hasUnlocked) {
                double gauge = ex.getCurrGauge();
                double cap = ex.getGaugeCapacity();
                if (gauge < cap) {
                    gauge = gauge + 1;
                    ex.setCurrGauge(gauge);
                }
            }
        }

        // If the player is the RECIPIENT of the attack
        if (event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entity;
            SCPlayer ex = SCPlayer.getPlayer(player);
            boolean hasUnlocked = ex.isEnabled();
            if (hasUnlocked) {
                double gauge = ex.getCurrGauge();
                double cap = ex.getGaugeCapacity();
                if (gauge < cap) {
                    gauge = gauge + 1;
                    ex.setCurrGauge(gauge);
                }
            }
        }
    }

    /**
     * Fills Spirit Gauge on update. Used in a few passive skills only.
     * @param event - Entity update event.
     */
    @SubscribeEvent
    public void onEntityTick(LivingUpdateEvent event){
        if(event.entity.worldObj.isRemote){
            return;
        }

        if (event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entity;
            SCPlayer ex = SCPlayer.getPlayer(player);
            boolean hasUnlocked = ex.isEnabled();
            if (hasUnlocked) {
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
}
