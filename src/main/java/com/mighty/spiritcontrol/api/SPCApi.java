package com.mighty.spiritcontrol.api;

import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.api.entity.IPlayer;

public class SPCApi {
    public SCPlayer getPlayer(IPlayer player){
        return SCPlayer.getPlayer((EntityPlayer) player.getMCEntity());
    }

    public AbilityDatabase getAbilityDatabse(){
        return AbilityDatabase.getInstance();
    }
}
