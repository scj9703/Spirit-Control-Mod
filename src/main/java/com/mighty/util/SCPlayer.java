package com.mighty.util;

import com.mighty.zsspiritcontrol.AbilityDatabase;
import com.mighty.zsspiritcontrol.PassiveAbility;
import com.mighty.zsspiritcontrol.zsspiritcontrol;
import java.util.ArrayList;
import java.text.DecimalFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import com.mighty.zsspiritcontrol.Attack;

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

    AbilityDatabase abilityDatabase = new AbilityDatabase();

    /**
     * The Spirit Gauge's default max capacity.
     */
    double gaugeCapacity = 1000;

    /**
     * The Spirit Gauge's current capacity
     */
     double currGauge = 0;

    /** The Player's Equipped Super Attack 1.
     * WIP!
     */
    Attack superAttack1;

    /** The Player's Equipped Super Attack 2.
     * WIP!
     */
    Attack superAttack2;

    /** The Player's Equipped Ultimate Attack.
     * WIP!
     */
    Attack ultimateAttack;

    /** The Player's Equipped Passive Ability.
     * WIP!
     */
    PassiveAbility passiveAbility;

    // The Player's unlocked attacks
    ArrayList<Attack> attacks = new ArrayList<>();
    // The Player's unlocked passives
    ArrayList<PassiveAbility> passives = new ArrayList<>();

    // Constructor
    public SCPlayer(){
        // Default abilities
        this.attacks.add(abilityDatabase.getKiAttack());
        this.attacks.add(abilityDatabase.getEnergyWave());
        this.passives.add(abilityDatabase.getVirtuousSpirit());
        this.superAttack1 = abilityDatabase.getKiAttack();
        this.superAttack2 = abilityDatabase.getKiAttack();
        this.ultimateAttack = abilityDatabase.getEnergyWave();
        this.passiveAbility = abilityDatabase.getVirtuousSpirit();
    }

    // Gets the Player's equipped SA 1.
    public Attack getSuperAttack1(){
        return superAttack1; }

    // Gets the Player's equipped SA 2.
    public Attack getSuperAttack2(){
        return superAttack2; }

    // Gets the Player's equipped Ultimate.
    public Attack getUltimateAttack(){
        return ultimateAttack; }

    // Gets the Player's equipped Passive.
    public PassiveAbility getPassiveAbility(){
        return passiveAbility; }

    // Gets the Player's unlocked Attacks.
    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    // Gets the Player's unlocked Passives.
    public ArrayList<PassiveAbility> getPassives() {
        return passives;
    }

    // Sets the Player's unlocked Attacks.
    public void setAttacks(ArrayList<Attack> attacks) {
        this.attacks = attacks;
    }

    // Sets the Player's unlocked Passives.
    public void setPassives(ArrayList<PassiveAbility> passives) {
        this.passives = passives;
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
        String gaugeString = "{"; // Left Border
        // Displays the 'fullness' of the Spirit Gauge.
        // Every 10% fills the meter's display
        for (int i = 1; i <= 10; i++){
            if (gauge < oneTenth*i){
                gaugeString = gaugeString + "-";
            }
            else {
                gaugeString = gaugeString + "=";
            }
        }
        gaugeString = gaugeString + "}"; // Right Border
        return gaugeString;
    }
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        // Saves the player's SC Data
        return;

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        // Loads the player's SC Data
        return;
    }

    @Override
    public void init(Entity entity, World world) {
    }

    /**
     * @param p The player in question
     * @return The sc properties of player 'p'
     */
    public static SCPlayer getPlayer(EntityPlayer p) {
        SCPlayer ex = (SCPlayer) p.getExtendedProperties(zsspiritcontrol.MODID);

        if (ex.player == null) {
            ex.player = p;
        }

        return ex;
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties("ZSSpiritcontrol", new SCPlayer());
    }
}
