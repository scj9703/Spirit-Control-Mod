package com.mighty.spiritcontrol.player;

import JinRyuu.JRMCore.JRMCoreH;
import com.mighty.spiritcontrol.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class DBCPlayerWrapper {

    EntityPlayer player;
    NBTTagCompound nbt;

    String statusEffects;

    public DBCPlayerWrapper(EntityPlayer player){
        this.player = player;
        this.nbt = player.getEntityData().getCompoundTag("PlayerPersisted");
    }

    public boolean isCharging(){
        return getStatusEffects().contains("A");
    }

    public boolean isFatigued(){
        return nbt.getInteger("jrmcGodStrain") != 0;
    }

    public void setFatigue(double timeInMinutes){
        nbt.setInteger("jrmcGodStrain", (int) (timeInMinutes*12));
    }

    public byte getForm() {
        return nbt.getByte("jrmcState");
    }

    public byte getRace() {
        return nbt.getByte("jrmcRace");
    }

    public String getStatusEffects(){;
        return nbt.getString("jrmcStatusEff");
    }

    public void updateStatusEffString(){
        statusEffects = getStatusEffects();
    }

    public boolean isFused() {
        updateStatusEffString();
        if(JRMCoreH.StusEfcts(10, statusEffects) || JRMCoreH.StusEfcts(11, statusEffects))
            return true;

        String[] fusionString = nbt.getString("jrmcFuzion").split(",");
        return fusionString.length == 3;
    }

    public boolean isController(){
        return JRMCoreH.StusEfcts(10, getStatusEffects());
    }

    public int getStat(int statId){
        int[] attributes = getAttributes();

        byte race = getRace();
        String racial = getRacialSkill();
        byte powerType = 1;
        byte release = 100;
        int pwrPoints = 0;
        boolean isFused = isFused();
        String[] skills = getSkills();
        String absorption = "0";

        byte state = 0;
        byte state2 = 0;

        if(Config.ACCEPT_RACIAL_BUFFS){
            if(race == 4)
                pwrPoints = getArcoReserves();
            if(race == 5)
                absorption = getMajinAbsorb();
        }

        if(Config.ACCEPT_RACIAL_FORMS){
            state = getForm();
        }


        boolean isLegendary = false;
        boolean isMajin = false;
        boolean isKK = false;
        boolean isMystic = false;
        boolean isUI = false;
        boolean isGoD = false;

        if(Config.ACCEPT_STATUS_EFF){
            updateStatusEffString();
            isLegendary = isLegendary();
            isMajin = isMajin();
        }
        if(Config.ACCEPT_NON_RACIAL){
            state2 = getState2();
            isKK = isKK();
            isMystic = isMystic();
            isUI = isUI();
            isGoD = isGoD();
        }

        return JRMCoreH.getPlayerAttribute(player, attributes, statId, state, state2, race, racial, release, pwrPoints, isLegendary, isMajin, isKK, isMystic, isUI, isGoD, powerType, skills, isFused, absorption);
    }

    private String[] getSkills() {
        return JRMCoreH.PlyrSkills(player);
    }

    private String getRacialSkill() {
        return nbt.getString("jrmcSSltX");
    }

    private boolean isGoD(){
        return JRMCoreH.StusEfcts(20, statusEffects);
    }

    private boolean isUI(){
        return JRMCoreH.StusEfcts(19, statusEffects);
    }

    private boolean isMystic(){
        return JRMCoreH.StusEfcts(13, statusEffects);
    }

    private boolean isKK(){
        return JRMCoreH.StusEfcts(5, statusEffects);
    }

    private byte getState2() {
        return nbt.getByte("jrmcState2");
    }

    private boolean isMajin() {
        return JRMCoreH.StusEfcts(13, statusEffects);
    }

    private boolean isLegendary() {
        return JRMCoreH.StusEfcts(14, statusEffects);
    }

    private String getMajinAbsorb() {
        return nbt.getString("jrmcMajinAbsorptionData");
    }

    private int getArcoReserves() {
        return nbt.getInteger("jrmcArcRsrv");
    }

    public int[] getAttributes(){
        if(!Config.ACCEPT_FUSION || !isFused())
            return getStats(player);

        String[] fusionPartners = nbt.getString("jrmcFuzion").split(",");

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

    public NBTTagCompound getNbt() {
        return nbt;
    }
}
