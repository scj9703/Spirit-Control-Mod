package com.mighty.zsspiritcontrol.util;

import com.mighty.zsspiritcontrol.attack.AbilityDatabase;
import com.mighty.zsspiritcontrol.attack.PassiveAbility;
import com.mighty.zsspiritcontrol.SpiritControl;
import java.util.ArrayList;
import java.text.DecimalFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import com.mighty.zsspiritcontrol.attack.Attack;
import net.minecraftforge.common.util.Constants;

/** Extended Player for Spirit Control **/
    public class SCPlayer implements IExtendedEntityProperties {
    /**
     * The Player's SC Data.
     */
    public NBTTagCompound data = null;

    /**
     * The player who this data belongs to
     */
    EntityPlayer player = null;

    /**
     * The Spirit Gauge's default max capacity.
     */
    double gaugeCapacity = 1000;

    /**
     * The Spirit Gauge's current capacity
     */
     double currGauge = 0;

    /** The Player's Equipped Super Attack 1.
     */
    Attack superAttack1;

    /** The Player's Equipped Super Attack 2.
     */
    Attack superAttack2;

    /** The Player's Equipped Ultimate Attack.
     */
    Attack ultimateAttack;

    /** The Player's Equipped Passive Ability.
     */
    PassiveAbility passiveAbility;

    // The Player's unlocked attacks
    ArrayList<Attack> attacks = new ArrayList<>();
    // The Player's unlocked passives
    ArrayList<PassiveAbility> passives = new ArrayList<>();

    boolean hasSpiritControl; // I.e., did the player unlock the mod's features

    // Constructor
    public SCPlayer(EntityPlayer player){
        // Default abilities
        this.attacks.add(AbilityDatabase.getAttackByName("KiAttack"));
        this.attacks.add(AbilityDatabase.getAttackByName("EnergyWave"));

        this.passives.add(AbilityDatabase.getPassiveByName("VirtuousSpirit"));

        this.superAttack1 = AbilityDatabase.getAttackByName("KiAttack");
        this.superAttack2 = AbilityDatabase.getAttackByName("KiAttack");
        this.ultimateAttack = AbilityDatabase.getAttackByName("EnergyWave");

        this.passiveAbility = AbilityDatabase.getPassiveByName("VirtuousSpirit");

        this.player = player;
        this.hasSpiritControl = false;
    }

    // Returns whether the player unlocked Spirit Control.
    public boolean isEnabled(){
        return hasSpiritControl;
    }

    // Enables/Disables Spirit Control.
    public void toggleSpiritControl(boolean toggle){
        this.hasSpiritControl = toggle; // True or false
    }

    // Gets the Player's equipped SA 1.
    public Attack getSuperAttack1(){
        return superAttack1; }

    // Sets/Equips SA1.
    public void setSuperAttack1(Attack superAttack1){
        this.superAttack1 = superAttack1;
    }

    // Gets the Player's equipped SA 2.
    public Attack getSuperAttack2(){
        return superAttack2; }

    // Sets/Equips SA2.
    public void setSuperAttack2(Attack superAttack2){
        this.superAttack2 = superAttack2;
    }

    // Gets the Player's equipped Ultimate.
    public Attack getUltimateAttack(){
        return ultimateAttack; }

    // Sets/Equips Ultimate.
    public void setUltimateAttack(Attack ultimateAttack){
        this.ultimateAttack = ultimateAttack;
    }

    // Gets the Player's equipped Passive.
    public PassiveAbility getPassiveAbility(){
        return passiveAbility; }

    // Sets/Equips Passive.
    public void setPassiveAbility(PassiveAbility passiveAbility){
        this.passiveAbility = passiveAbility;
    }

    // Gets the Player's unlocked Attacks.
    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    // Gets the Player's unlocked Passives.
    public ArrayList<PassiveAbility> getPassives() {
        return passives;
    }

    // Sets the Player's unlocked Attacks, preventing duplicates.
    public void setAttacks(ArrayList<Attack> attacks) {
        ArrayList<String> dupeChecker = new ArrayList<>();
        ArrayList<Attack> attacksWithoutDupes = new ArrayList<>();
        for (Attack attack : attacks){
            if (!dupeChecker.contains(attack.getName())){
                dupeChecker.add(attack.getName());
                attacksWithoutDupes.add(attack);
            }
        }
        this.attacks = attacksWithoutDupes;
    }

    // Sets the Player's unlocked Passives.
    public void setPassives(ArrayList<PassiveAbility> passives) {
        ArrayList<String> dupeChecker = new ArrayList<>();
        ArrayList<PassiveAbility> passivesWithoutDupes = new ArrayList<>();
        for (PassiveAbility passive : passives){
            if (!dupeChecker.contains(passive.getName())){
                dupeChecker.add(passive.getName());
                passivesWithoutDupes.add(passive);
            }
        }
        this.passives = passivesWithoutDupes;
    }

    /**
     * Returns the maximum Spirit Gauge Capacity.
     * @return int max capacity
     */
    public double getGaugeCapacity() {
        return gaugeCapacity;
    }

    /**
     * Returns the current Spirit Gauge capacity.
     * @return int curr capacity
     */
    public double getCurrGauge(){
        return currGauge;
    }

    /**
     * Sets the player's current Gauge. Used to fill it, i.e. by hitting things.
     * Prints the Gauge every 5%.
     * @param newVal - Value of the new Gauge.
     */
    public void setCurrGauge(double newVal){
        if (newVal > gaugeCapacity){
            newVal = gaugeCapacity; // Caps the Gauge if you go over max
        }
        else if (newVal % 50 == 0){
            double percent = (newVal / getGaugeCapacity()) * 100;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedPercent = decimalFormat.format(percent);
            String chatGauge = printGauge();
            player.addChatComponentMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA + "==> " + chatGauge + " Your Spirit Gauge is at " + formattedPercent + " Percent Capacity."));
        }
        currGauge = newVal;
    }

    /**
     * Prints a display version of the Spirit Gauge. Used for display to players.
     * I.e. {======----} = 69%
     * @return String version of Spirit Gauge.
     */
    public String printGauge(){
        double gauge = getCurrGauge();
        double cap = getGaugeCapacity();
        double oneTenth = cap/10;
        StringBuilder gaugeString = new StringBuilder("{"); // Left Border
        // Displays the 'fullness' of the Spirit Gauge.
        // Every 10% fills the meter's display
        for (int i = 1; i <= 10; i++){
            if (gauge < oneTenth*i){
                gaugeString.append("-");
            }
            else {
                gaugeString.append("=");
            }
        }
        gaugeString.append("}"); // Right Border
        return gaugeString.toString();
    }
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        // Saves the player's SC Data

        NBTTagCompound scTag = new NBTTagCompound();

        scTag.setBoolean("hasSpiritControl", hasSpiritControl);

        scTag.setDouble("gaugeCapacity", gaugeCapacity);
        scTag.setDouble("currGauge", currGauge);

        scTag.setString("selectedAttack1", superAttack1.getName());
        scTag.setString("selectedAttack2", superAttack2.getName());
        scTag.setString("selectedUltimate", ultimateAttack.getName());
        scTag.setString("selectedPassive", passiveAbility.getName());

        NBTTagList attackList = new NBTTagList();
        for(Attack att : getAttacks()){
            attackList.appendTag(new NBTTagString(att.getName()));
        }
        scTag.setTag("unlockedAttacks", attackList);

        NBTTagList passiveList = new NBTTagList();
        for(PassiveAbility pass : getPassives()){
            passiveList.appendTag(new NBTTagString(pass.getName()));
        }
        scTag.setTag("unlockedPassives", passiveList);

        compound.setTag("SpiritControl", scTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        //Loads a players data

        if(!compound.hasKey("SpiritControl")){
            return;
        }

        NBTTagCompound scTag = compound.getCompoundTag("SpiritControl");

        hasSpiritControl = scTag.getBoolean("hasSpiritControl");

        gaugeCapacity = scTag.getDouble("gaugeCapacity");
        currGauge = scTag.getDouble("currGauge");


        Attack att1 = AbilityDatabase.getAttackByName(scTag.getString("selectedAttack1"));
        superAttack1 = att1 != null ? att1 : superAttack1;

        Attack att2 = AbilityDatabase.getAttackByName(scTag.getString("selectedAttack2"));
        superAttack2 = att2 != null ? att2 : superAttack2;

        Attack attUlt = AbilityDatabase.getAttackByName(scTag.getString("selectedUltimate"));
        ultimateAttack = attUlt != null ? attUlt : ultimateAttack;

        PassiveAbility pass = AbilityDatabase.getPassiveByName(scTag.getString("selectedPassive"));
        passiveAbility = pass != null ? pass : passiveAbility;


        NBTTagList attackList = scTag.getTagList("unlockedAttacks", Constants.NBT.TAG_STRING);
        ArrayList<Attack> newAbilities = new ArrayList<>();
        for(int i = 0; i < attackList.tagCount(); i++){
            newAbilities.add(AbilityDatabase.getAttackByName(attackList.getStringTagAt(i)));
        }
        this.setAttacks(newAbilities);

        NBTTagList passiveList = scTag.getTagList("unlockedPassives", Constants.NBT.TAG_STRING);
        ArrayList<PassiveAbility> newPassives = new ArrayList<>();
        for(int i = 0; i < passiveList.tagCount(); i++){
            newPassives.add(AbilityDatabase.getPassiveByName(passiveList.getStringTagAt(i)));
        }
        this.setPassives(newPassives);
    }

    @Override
    public void init(Entity entity, World world) {
    }

    /**
     * @param p The player in question
     * @return The sc properties of player 'p'
     */
    public static SCPlayer getPlayer(EntityPlayer p) {
        return (SCPlayer) p.getExtendedProperties(SpiritControl.MODID);
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(SpiritControl.MODID, new SCPlayer(player));
    }

    /**
     * Copy over data from a player provided in the argument
     * @param otherPlayer SCPlayer of the player you want to copy data of
     */
    public void copy(SCPlayer otherPlayer) {
        NBTTagCompound nbt = new NBTTagCompound();
        otherPlayer.saveNBTData(nbt);
        this.loadNBTData(nbt);
    }
}
