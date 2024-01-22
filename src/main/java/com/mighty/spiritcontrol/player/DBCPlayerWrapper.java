package com.mighty.spiritcontrol.player;

import JinRyuu.DragonBC.common.DBC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class DBCPlayerWrapper {

    EntityPlayer player;
    NBTTagCompound compound;

    public DBCPlayerWrapper(EntityPlayer player){
        this.player = player;
        this.compound = player.getEntityData().getCompoundTag("PlayerPersisted");
    }

    public boolean isCharging(){
        return compound.getString("jrmcStatusEff").contains("A");
    }

    public boolean isFatigued(){
        return compound.getInteger("jrmcGodStrain") != 0;
    }

    public void setFatigue(double timeInMinutes){
        compound.setInteger("jrmcGodStrain", (int) (timeInMinutes*5*60));
    }

    public byte getForm() {
        return compound.getByte("jrmcState");
    }

    public byte getRace() {
        return compound.getByte("jrmcRace");
    }
}
