package com.mighty.zsspiritcontrol.event;

import com.mighty.zsspiritcontrol.ability.passive.EnumFillMethod;
import com.mighty.zsspiritcontrol.ability.passive.PassiveAbility;
import com.mighty.zsspiritcontrol.player.SCPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerEventHandler {
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
    public void onEntityAttack(LivingAttackEvent event) {
        //
    }

    @SubscribeEvent
    public void onEntityTakenDamage(LivingHurtEvent event){
        if(event.entity.worldObj.isRemote){ //Return if even ran on client
            return;
        }

        if(event.isCanceled()){
            return;
        }

        // If the player is the CAUSE of the attack
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            SCPlayer ex = SCPlayer.getPlayer(player);

            if((!ex.hasUnlockedSpiritControl()) || ex.isFatigued())
                return;

            PassiveAbility passive = (PassiveAbility) ex.getAbilityFromSlot("passive");
            if (passive.canPassiveFillLikeThis(EnumFillMethod.DAMAGE_DEALT) && passive.canPlayerUsePassive(ex)  && !ex.isFatigued()) {
                //ex.addChatMessage(new ChatComponentText("This is from dealing dmg"));
                ex.addSpirit(1);
            }
        }

        // If the player is the RECIPIENT of the attack
        if (event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entity;
            SCPlayer ex = SCPlayer.getPlayer(player);

            if(!ex.hasUnlockedSpiritControl())
                return;

            PassiveAbility passive = (PassiveAbility) ex.getAbilityFromSlot("passive");
            if (passive.canPassiveFillLikeThis(EnumFillMethod.DAMAGE_TAKEN) && passive.canPlayerUsePassive(ex) && !ex.isFatigued()) {
                //ex.addChatMessage(new ChatComponentText("This is from taking dmg"));
                ex.addSpirit(1);
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
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }


        EntityPlayer player = (EntityPlayer) event.entity;
        SCPlayer extPlayer = SCPlayer.getPlayer(player);

        //Return if player hasn't unlocked SC
        if(!extPlayer.hasUnlockedSpiritControl()){
            return;
        }
        
        handleSpiritControlArming(extPlayer);

        if(extPlayer.isFatigued())
            return;

        if (extPlayer.canPassiveFillLikeThis(EnumFillMethod.PASSIVE) && extPlayer.canPlayerUsePassive()) {
            //ex.addChatMessage(new ChatComponentText("This is from passive"));
            extPlayer.addSpirit(1);
        }

        //extPlayer.addSpirit(0.01);


    }

    private void handleSpiritControlArming(SCPlayer extPlayer) {
        if(!extPlayer.isSneaking()){
            extPlayer.wasSneakingLastTick = false;
            return;
        }

        if(!extPlayer.wasSneakingLastTick) {
            long time = System.currentTimeMillis();
            if(time - extPlayer.lastTimeSneaked <= 420){
                extPlayer.sneakCount += 1;
            }else{
                extPlayer.sneakCount = 1;
            }
            extPlayer.lastTimeSneaked = time;
        }

        if(extPlayer.sneakCount >= 3)
            extPlayer.toggleIsArmed();


        extPlayer.wasSneakingLastTick = true;

    }
}
