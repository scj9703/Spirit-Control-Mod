package com.mighty.spiritcontrol.event;

import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.EnumFillMethod;
import com.mighty.spiritcontrol.config.Config;
import com.mighty.spiritcontrol.event.custom.PlayerSneakEvent;
import com.mighty.spiritcontrol.event.custom.PlayerSwingEvent;
import com.mighty.spiritcontrol.player.SCPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import somehussar.minimessage.MiniMessageParser;

public class SpiritControlHandler {
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

    /**
     * Copies over SC data to the player whenever he's cloned due to dimension changes, etc.
     * @param event
     */
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        SCPlayer.getPlayer(event.entityPlayer).copy(SCPlayer.getPlayer(event.original));
    }

    /**
     * Adds Spirit to the Player's Gauge on attack or when hit.
     * @param event - Event when a living entity is attacked.
     */
    @SubscribeEvent
    public void onPlayerSneak(PlayerSneakEvent event) {
        if(event.player.worldObj.isRemote || !event.sneaked)
            return;

        SCPlayer extPlayer = SCPlayer.getPlayer(event.player);
        if(!extPlayer.hasUnlockedSpiritControl())
            return;

        long time = MinecraftServer.getSystemTimeMillis();
        if(time - extPlayer.lastTimeSneaked <= 420)
            extPlayer.sneakCount += 1;
        else
            extPlayer.sneakCount = 1;

        extPlayer.lastTimeSneaked = time;
        if(extPlayer.sneakCount >= 3)
            extPlayer.toggleIsArmed();
    }

    @SubscribeEvent
    public void onPlayerSwing(PlayerSwingEvent event){
        if(event.player.worldObj.isRemote || !event.player.isSneaking())
            return;

        SCPlayer extPlayer = SCPlayer.getPlayer(event.player);
        if(!extPlayer.hasUnlockedSpiritControl() || !extPlayer.isArmed() || extPlayer.isChargingAttack)
            return;

        extPlayer.setCurrentAttackSlot(event.player.inventory.currentItem);

    }


    @SubscribeEvent
    public void onEntityTakenDamage(LivingHurtEvent event){
        if(event.entity.worldObj.isRemote || event.isCanceled()) //Return if even ran on client or cancelled
            return;

        // If the player is the CAUSE of the attack
        if (event.source.getEntity() instanceof EntityPlayer) {
            handlePassiveFilling((EntityPlayer) event.source.getEntity(), EnumFillMethod.DAMAGE_DEALT, Config.SPIRIT_ON_DAMAGE_DEALT_FLAT);
        }

        // If the player is the RECIPIENT of the attack
        if (event.entity instanceof EntityPlayer){
            handlePassiveFilling((EntityPlayer) event.entity, EnumFillMethod.DAMAGE_TAKEN, Config.SPIRIT_ON_DAMAGE_TAKEN_FLAT);
        }
    }

    /**
     * Fills Spirit Gauge on update. Used in a few passive skills only.
     * @param event - Entity update event.
     */
    @SubscribeEvent
    public void onEntityTick(LivingUpdateEvent event){
        if(event.entity.worldObj.isRemote)
            return;

        if(event.entity.worldObj.getTotalWorldTime() % 10 != 0)
            return;

        if (!(event.entity instanceof EntityPlayer))
            return;

        SCPlayer extPlayer = SCPlayer.getPlayer((EntityPlayer) event.entity);
        if(!extPlayer.hasUnlockedSpiritControl())
            return;

        handlePassiveFilling(extPlayer, EnumFillMethod.PASSIVE, Config.SPIRIT_PASSIVE_FLAT);
        handleCharging(extPlayer);
    }

    public void handlePassiveFilling(EntityPlayer player, EnumFillMethod method, double amount){
        handlePassiveFilling(SCPlayer.getPlayer(player), method, amount);
    }

    /**
     * Ensures proper filling of the gauge according to your passive ability
     * @param ex Player reference
     * @param method Type of method the passive is filled with (passively, by attacking or by being attacked)
     * @param amount Amount of flat spirit to give to the player (later adjusted by passive)
     */
    public void handlePassiveFilling(SCPlayer ex, EnumFillMethod method, double amount){

        if(ex.isFatigued() || ex.isChargingAttack)
            return;

        if (ex.canPlayerUsePassive(method)) {
            ex.addSpirit(amount);
        }
    }

    public void handleCharging(SCPlayer ex){
        if( !ex.player.isSneaking() || !ex.isArmed() || !ex.isChargingDBC() || !ex.canUseAttack()){
            ex.isChargingAttack = false;
            ex.startedCharging = 0;
            return;
        }

        Attack attack = ex.getCurrentSelectedAttack();
        long currentTime = MinecraftServer.getSystemTimeMillis();
        ex.isChargingAttack = true;

        if(ex.startedCharging <= 0)
            ex.startedCharging = currentTime;

        float percent = (float) ((float) (currentTime - ex.startedCharging) / 1000 / attack.getCasttime());
        byte roundedPercentToHighest10 = (byte) (Math.round(percent*10)*10);

        prettyChargeMessage(ex, attack, roundedPercentToHighest10);
        if(roundedPercentToHighest10 >= 100) {
            ex.addChatMessage(MiniMessageParser.getFormat(attack.getFireMessage()));
            ex.setCooldown(attack.getCooldown());
            ex.setFatigue(attack.getFatigue());
        }

    }

    private void prettyChargeMessage(SCPlayer ex, Attack attack, byte roundedPercentToHighest10) {
        if(roundedPercentToHighest10 > 100)
            roundedPercentToHighest10 = 100;
        ex.addChatMessage(MiniMessageParser.getFormat("<aqua>==><dark_aqua> Charging <aqua><attack_name> <gray>: <aqua><percent>%", "attack_name", attack.getName(), "percent", String.valueOf(roundedPercentToHighest10)));
    }
}
