package com.mighty.spiritcontrol.mixin;

import com.mighty.spiritcontrol.event.custom.PlayerSneakEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "setSneaking", at=@At("HEAD"))
    public void onEntitySneak(boolean isSneaking, CallbackInfo ci){
        try {
            //Try catch because we're only interested in players doing this, not all other entities.
            //The intermediate cast may result in an exception if it's not a player sneaking.
            MinecraftForge.EVENT_BUS.post(new PlayerSneakEvent((EntityPlayer) (Object) this, isSneaking));
        } catch (Exception ignored){

        }
    }
}
