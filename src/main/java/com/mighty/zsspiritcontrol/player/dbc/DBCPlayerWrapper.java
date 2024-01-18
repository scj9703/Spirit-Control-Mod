package com.mighty.zsspiritcontrol.player.dbc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class DBCPlayerWrapper {
    private EntityPlayer player;
    private NBTTagCompound forgeTag;

    public DBCPlayerWrapper(EntityPlayer player){
        this.player = player;
        this.forgeTag = player.getEntityData();
    }

    public boolean isFatigued(){
        return this.getFatigue() != 0;
    }

    public int getFatigue(){
        return forgeTag.getInteger("jrmcGodStrain");
    }

    public void setFatigue(int timeInSeconds){
        this.forgeTag.setInteger("jrmcGodStrain", timeInSeconds*5);
    }

}
