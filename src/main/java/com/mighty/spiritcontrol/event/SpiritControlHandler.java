package com.mighty.spiritcontrol.event;

import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.EnumFillMethod;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import com.mighty.spiritcontrol.config.Config;
import com.mighty.spiritcontrol.event.custom.PlayerSneakEvent;
import com.mighty.spiritcontrol.event.custom.PlayerSwingEvent;
import com.mighty.spiritcontrol.player.SCPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import somehussar.minimessage.MMParser;
import somehussar.minimessage.util.Util;

import java.text.DecimalFormat;

public class SpiritControlHandler {
    /**
     * Registers a player for Spirit Control attributes.
     * @param event
     */
    @SubscribeEvent
    public void entityConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && SCPlayer.getPlayer((EntityPlayer) event.entity) == null)
            SCPlayer.register((EntityPlayer)event.entity);

    }

    /**
     * Copies over SC data to the player whenever he's cloned due to dimension changes, etc.
     * @param event
     */
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        SCPlayer player = SCPlayer.getPlayer(event.entityPlayer);
        player.copy(SCPlayer.getPlayer(event.original));

        if(event.wasDeath)
            player.setSpirit(player.getSpirit()*Config.DEATH_PERSIST_PERCENTILE);
    }

    /**
     * Event responsible for triple shift-clicking arming/disarming
     * @param event
     */
    @SubscribeEvent
    public void onPlayerSneak(PlayerSneakEvent event) {
        if(event.player.worldObj.isRemote || !event.sneaked)
            return;

        SCPlayer extPlayer = SCPlayer.getPlayer(event.player);

        if(extPlayer.dbcPlayer.isFused() && !extPlayer.dbcPlayer.isController())
            return;

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

        if(event.source.getDamageType().equals("thorns"))
            return;

        // If the player is the CAUSE of the attack
        if (event.source.getEntity() instanceof EntityPlayer)
            handlePassiveFilling((EntityPlayer) event.source.getEntity(), EnumFillMethod.DAMAGE_DEALT, Config.SPIRIT_ON_DAMAGE_DEALT_FLAT);


        // If the player is the RECIPIENT of the attack, and that the cause of the attack was from an entity
        if (event.entity instanceof EntityPlayer && event.source.getEntity() != null) {
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

        if (!(event.entity instanceof EntityPlayer))
            return;

        //SC runs on 10 tick cycles, early returns every time its not a DBC tick.
        if(event.entity.worldObj.getTotalWorldTime() % 10 != 0)
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
     * @param extPlayer Player reference
     * @param method Type of method the passive is filled with (passively, by attacking or by being attacked)
     * @param amount Amount of flat spirit to give to the player (later adjusted by passive)
     */
    public void handlePassiveFilling(SCPlayer extPlayer, EnumFillMethod method, double amount){

        if(!extPlayer.hasUnlockedSpiritControl() || extPlayer.isFatigued() || extPlayer.isChargingAttack || (extPlayer.dbcPlayer.isFused() && !extPlayer.dbcPlayer.isController()))
            return;

        final long time = MinecraftServer.getSystemTimeMillis();
        if(time - extPlayer.lastTimeGainedSpirit < 100){
            return;
        }

        extPlayer.lastTimeGainedSpirit = time;

        if (extPlayer.canPlayerUsePassive(method)) {
            extPlayer.addSpiritByPassive(amount, method);
            return;
        }

        double passiveModifier = ( (PassiveAbility) extPlayer.getAbilityFromSlot("passive")).getSpiritFillModifier(method);
        if(passiveModifier < 1.0)
            amount *= passiveModifier;
        extPlayer.addSpirit(amount);
    }

    /**
     * Handles charging of SC attacks
     * @param extPlayer player that is trying to charge an attack
     */
    public void handleCharging(SCPlayer extPlayer){
        if( !extPlayer.player.isSneaking() || !extPlayer.isChargingDBC()){
            setDefaultChargingState(extPlayer);
            return;
        }

        if(!extPlayer.isArmed()){
            //Extra if to only display messages once per charge attempt
            if(extPlayer.startedCharging > -1) {
                extPlayer.addChatMessage(MMParser.getFormat("<aqua>==> <dark_aqua>You need to be <green>armed</green> to use SC Attacks!"));
                setDefaultChargingState(extPlayer);
            }
            extPlayer.startedCharging = -1;
            return;
        }

        if(extPlayer.getCurrentSelectedAttack().isUltimate() && extPlayer.isFatigued()){
            //Extra if to only display messages once per charge attempt
            if(extPlayer.startedCharging > -1) {
                extPlayer.addChatMessage(MMParser.getFormat("<aqua>==> <dark_aqua>You <dark_red>can't</dark_red> use ultimates when fatigued!"));
                setDefaultChargingState(extPlayer);
            }
            extPlayer.startedCharging = -1;
            return;
        }

        if(extPlayer.isOnCooldown()){
            //Extra if to only display messages once per charge attempt
            DecimalFormat format = new DecimalFormat("#.##");
            if(extPlayer.startedCharging > -1) {
                extPlayer.addChatMessage(MMParser.getFormat("<aqua>==> <dark_aqua>You're on a <dark_red>cooldown</dark_red> before you can use another ability for the next <time> seconds!", "time", format.format(extPlayer.getCooldown())));
                setDefaultChargingState(extPlayer);
            }
            extPlayer.startedCharging = -1;
            return;
        }

        if(!extPlayer.canUseAttack()){
            //Extra if to only display messages once per charge attempt
            if(extPlayer.startedCharging > -1) {
                extPlayer.addChatMessage(MMParser.getFormat("<aqua>==> <dark_aqua>You <dark_red>don't</dark_red> have enough Spirit to use this attack!"));
                setDefaultChargingState(extPlayer);
            }
            extPlayer.startedCharging = -1;
            return;
        }

        //Early return if the player is fused but is NOT the controller.
        if(extPlayer.dbcPlayer.isFused() && !extPlayer.dbcPlayer.isController()){
            return;
        }

        Attack attack = extPlayer.getCurrentSelectedAttack();
        long currentTime = MinecraftServer.getSystemTimeMillis();
        extPlayer.isChargingAttack = true;

        if(extPlayer.startedCharging <= 0)
            extPlayer.startedCharging = currentTime;

        double percent = ((double) (currentTime - extPlayer.startedCharging) / 1000) / attack.getCasttime();
        percent = Math.min(Math.max(0, percent), 1); //Gets rid of some pesky bugs with percentiles going above the max size of a signed byte (127)
        byte roundedPercentToHighest10 = (byte) (Math.round(percent*10)*10);

        prettyChargeMessage(extPlayer, attack, roundedPercentToHighest10);
        if(roundedPercentToHighest10 >= 100) {
            attack.fire(extPlayer);
            extPlayer.startedCharging = 0;
            extPlayer.lastPrintedCharge = -1;
        }

    }

    /**
     * Resets the charge status of a player.
     * @param extPlayer SCPlayer reference
     */
    private void setDefaultChargingState(SCPlayer extPlayer){
        extPlayer.isChargingAttack = false;
        extPlayer.startedCharging = 0;
        extPlayer.lastPrintedCharge = -1;
    }

    /**
     * Updates a player about their charging status
     * @param extPlayer Player that is trying to charge
     * @param attack Attack they're charging
     * @param roundedPercentToHighest10 The percent they just charged their attack to (rounded to the nearest 10)
     */
    private void prettyChargeMessage(SCPlayer extPlayer, Attack attack, byte roundedPercentToHighest10) {
        if(roundedPercentToHighest10 <= extPlayer.lastPrintedCharge)
            return;
        if(roundedPercentToHighest10 > 100)
            roundedPercentToHighest10 = 100;
        extPlayer.lastPrintedCharge = roundedPercentToHighest10;
        extPlayer.addChatMessage(MMParser.getFormat("<aqua>==><dark_aqua> Charging <aqua><attack_hoverable> <gray>: <aqua><percent>%", "attack_hoverable", Util.getAbilityHover(attack), "percent", String.valueOf(roundedPercentToHighest10)));
    }
}
