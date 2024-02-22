package com.mighty.spiritcontrol.player;

import JinRyuu.JRMCore.ComJrmcaBonus;
import JinRyuu.JRMCore.JRMCoreH;
import JinRyuu.JRMCore.server.JGMathHelper;
import JinRyuu.JRMCore.server.config.dbc.JGConfigRaces;
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

    public byte getClassId() {
        return nbt.getByte("jrmcClass");
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
        int bonus = 0;

        if(Config.ACCEPT_STAT_BONUSES) {
            bonus = getBonusAttribute(statId, attributes[statId]);
        }

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
            isMystic = isMystic();
            isUI = isUI();
            isGoD = isGoD();
        }
        if(Config.ACCEPT_KAIOKEN){
            state2 = getState2();
            isKK = isKK();
        }

        return JRMCoreH.getPlayerAttribute(player, attributes, statId, state, state2, race, racial, release, pwrPoints, isLegendary, isMajin, isKK, isMystic, isUI, isGoD, powerType, skills, isFused, absorption) + bonus;
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

    private int getBonusAttribute(int statId, int baseStat) {
        String nbtValue = nbt.getString("jrmcAttrBonus" + ComJrmcaBonus.ATTRIBUTES_SHORT[statId]);
        int bonusAttribute = baseStat;

        if(!nbtValue.equals("NONE") && !nbtValue.equals("n")){
            double bonusValueResult = bonusAttribute;
            String[] bonus = nbtValue.split("\\|");
            String[][] bonusValues = new String[bonus.length][2];
            if (bonus.length > 0 && !bonus[0].isEmpty()) {
                for(int i = 0; i < bonus.length; ++i) {
                    if (bonus[i].length() > 1) {
                        String[] bonusValue = bonus[i].split("\\;");
                        bonusValues[i][1] = bonusValue[1];

                        double value2;
                        try {
                            value2 = Double.parseDouble(bonusValues[i][1].substring(1));

                            String operator = bonusValues[i][1].substring(0, 1);
                            if(operator.equals("+") || operator.equals("-"))
                                value2 =  value2 / JGConfigRaces.CONFIG_RACES_STATS_MULTI[getRace()][getClassId()][statId];

                            bonusValueResult = JGMathHelper.StringMethod(operator, bonusValueResult, value2);
                        } catch (Exception ignored) {

                        }
                    }
                }
            }

            bonusAttribute = (int)bonusValueResult;
        }

        return bonusAttribute;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    public int[] getFormAttributes() {
        int[] stats = new int[6];
        for(int i = 0; i < 6; i++) {
            stats[i] = getStat(i);
        }
        return stats;
    }

    public int getEffectiveLevel(){
        int[] stats = getFormAttributes();

        int level = 0;
        for(int i = 0; i < 6; i++){
            level += stats[i]/5;
        }

        level -= 11;

        if(level < 1)
            level = 1;

        return level;
    }
}
