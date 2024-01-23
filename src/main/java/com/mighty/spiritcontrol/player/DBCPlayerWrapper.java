package com.mighty.spiritcontrol.player;

import JinRyuu.JRMCore.JRMCoreH;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class DBCPlayerWrapper {

    EntityPlayer player;
    NBTTagCompound compound;

    public DBCPlayerWrapper(EntityPlayer player){
        this.player = player;
        this.compound = player.getEntityData().getCompoundTag("PlayerPersisted");
    }

    public boolean isCharging(){
        return getStatusEffects().contains("A");
    }

    public boolean isFatigued(){
        return compound.getInteger("jrmcGodStrain") != 0;
    }

    public void setFatigue(double timeInMinutes){
        compound.setInteger("jrmcGodStrain", (int) (timeInMinutes*20));
    }

    public byte getForm() {
        return compound.getByte("jrmcState");
    }

    public byte getRace() {
        return compound.getByte("jrmcRace");
    }

    public String getStatusEffects(){
        return compound.getString("jrmcStatusEff");
    }

    public boolean isFused() {
        if(JRMCoreH.StusEfcts(10, getStatusEffects()) || JRMCoreH.StusEfcts(11, getStatusEffects()))
            return true;

        String[] fusionString = compound.getString("jrmcFuzion").split(",");
        return fusionString.length == 3;
    }

    public int[] getAttributes(){

        if(!isFused())
            return getStats(player);

        String[] fusionPartners = compound.getString("jrmcFuzion").split(",");

        EntityPlayer player1 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(fusionPartners[0]);
        EntityPlayer player2 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(fusionPartners[1]);

        if(player1 == null || player2 == null)
            return getStats(player);

        int[] stats1 = getStats(player1);
        int[] stats2 = getStats(player2);

        int[] fusedStats = new int[6];

        for(int i = 0; i < stats1.length; i++){
            fusedStats[i] = Math.min(stats1[i], stats2[i]) * 2;
        }

        return fusedStats;
    }

    private int[] getStats(EntityPlayer player) {
        NBTTagCompound nbt = JRMCoreH.nbt(player);
        int[] stats = new int[6];
        String[] attr = { "jrmcStrI", "jrmcDexI", "jrmcCnsI", "jrmcWilI", "jrmcIntI", "jrmcCncI" };

        for (int i = 0; i < attr.length; i++) {
            stats[i] = nbt.getInteger(attr[i]);
        }

        return stats;
    }

}
