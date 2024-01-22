package com.mighty.spiritcontrol.mixin;

import com.mighty.spiritcontrol.event.custom.PlayerSwingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Inject(method = "swingItem", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;getEntityTracker()Lnet/minecraft/entity/EntityTracker;"))
    public void onSwing(CallbackInfo ci){
        try {
            //Try catch because we're only interested in players doing this, not all other entities.
            //The intermediate cast may result in an exception if it's not a player swinging.
            MinecraftForge.EVENT_BUS.post(new PlayerSwingEvent((EntityPlayer) (Object) this));
        } catch (Exception ignored) {

        }

    }
}
