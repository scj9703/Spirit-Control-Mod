package com.mighty.zsspiritcontrol.player;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.ability.Ability;
import com.mighty.zsspiritcontrol.ability.AbilityDatabase;
import com.mighty.zsspiritcontrol.ability.Attack;
import com.mighty.zsspiritcontrol.ability.PassiveAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import somehussar.minimessage.MiniMessageParser;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class SCPlayer implements IExtendedEntityProperties {

    /**
     * Player reference
     */
    private final EntityPlayer player;

    /**
     * DBCPlayerWrapper.
     *
     * Hopefully will move this to be a ZS lib class later on.
     */
    private final DBCPlayerWrapper dbcPlayer;
    /**
     * A check if the player can receieve messages.
     * <br><br>
     * If the player isn't fully loaded (hasn't finished connecting),
     * the game/server will crash for trying to send a message through an uninitialized connection
     */
    private boolean canReceiveMessages = false;

    /**
     * Gauge info
     */
    private double maxBaseSpirit = 1000;
    private double currentSpirit = 0;

    /**
     * Used for sending a message to the player every 5% gauge fill updates reliably.
     */
    private byte lastPercentPrinted = 0;

    /**
     * Selected abilities
     */
    private Attack superAttack1;
    private Attack superAttack2;
    private Attack ultimateAttack;
    private PassiveAbility passiveAbility;

    /**
     * Unlocked abilities
     * <p>
     * Sets do not allow duplicate values.
     */
    private Set<Attack> unlockedSuperAttacks = new HashSet<>();
    private Set<Attack> unlockedUltimates = new HashSet<>();
    private Set<PassiveAbility> unlockedPassives = new HashSet<>();

    /**
     * Has unlocked Spirit Control yet?
     */
    private boolean unlockedSpiritControl = false;

    public SCPlayer(EntityPlayer player){
        canReceiveMessages = false;
        this.player = player;
        this.dbcPlayer = new DBCPlayerWrapper(player);

        Attack kiAttack = (Attack) AbilityDatabase.getAbilityByName("KiAttack");
        Attack energyWave = (Attack) AbilityDatabase.getAbilityByName("EnergyWave");
        PassiveAbility virtuousSpirit = (PassiveAbility) AbilityDatabase.getAbilityByName("VirtuousSpirit");

        this.addAbility(kiAttack);
        this.addAbility(energyWave);
        this.addAbility(virtuousSpirit);

        this.setAbilityAtSlot(kiAttack, "super1");
        this.setAbilityAtSlot(kiAttack, "super2");
        this.setAbilityAtSlot(energyWave, "ultimate");
        this.setAbilityAtSlot(virtuousSpirit, "passive");

    }
    public static SCPlayer getPlayer(EntityPlayer player){
        return (SCPlayer) player.getExtendedProperties(SpiritControl.MODID);
    }
    public static void register(EntityPlayer player){
        if(getPlayer(player) == null)
            player.registerExtendedProperties(SpiritControl.MODID, new SCPlayer(player));
    }
    public void copy(SCPlayer otherPlayer){
        NBTTagCompound nbt = new NBTTagCompound();
        otherPlayer.saveNBTData(nbt);
        this.loadNBTData(nbt);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound scTag = new NBTTagCompound();

        scTag.setBoolean("hasUnlocked", this.isEnabled());

        scTag.setDouble("maxSpirit", this.getMaxBaseSpirit());
        scTag.setDouble("currentSpirit", this.getSpirit());

        scTag.setString("Super1", this.getAbilityFromSlot("Super1").getName());
        scTag.setString("Super2", this.getAbilityFromSlot("Super2").getName());
        scTag.setString("Ultimate", this.getAbilityFromSlot("Ultimate").getName());
        scTag.setString("Passive", this.getAbilityFromSlot("Passive").getName());

        NBTTagList superList = new NBTTagList();
        for(Attack att : this.getAttacks())
            superList.appendTag(new NBTTagString(att.getName()));
        scTag.setTag("Supers", superList);

        NBTTagList ultimateList = new NBTTagList();
        for(Attack att : this.getUltimates())
            ultimateList.appendTag(new NBTTagString(att.getName()));
        scTag.setTag("Ultimates", ultimateList);

        NBTTagList passiveList = new NBTTagList();
        for(PassiveAbility passive : this.getPassives())
            passiveList.appendTag(new NBTTagString(passive.getName()));
        scTag.setTag("Passives", passiveList);

        compound.setTag("SpiritControl", scTag);

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if(!compound.hasKey("SpiritControl")){
            return;
        }

        canReceiveMessages = false; //Disables updates messages while loading the player (dimension changes, relogs)

        NBTTagCompound scTag = compound.getCompoundTag("SpiritControl");

        this.setUnlockedSpiritControl(scTag.getBoolean("hasUnlocked"));

        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Super1")), "super1");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Super2")), "super2");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Ultimate")), "ultimate");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Passive")), "passive");

        this.setMaxBaseSpirit(scTag.getDouble("maxSpirit"));
        this.setSpirit(scTag.getDouble("currentSpirit"));

        this.canReceiveMessages = true;
    }

    public void addChatMessage(IChatComponent chatComponent){
        if(this.canReceiveMessages)
            player.addChatMessage(chatComponent);
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public void setUnlockedSpiritControl(boolean shouldUnlock){
        this.unlockedSpiritControl = shouldUnlock;
    }

    public boolean isEnabled(){
        return this.unlockedSpiritControl;
    }

    public Set<Attack> getAttacks(){
        return this.unlockedSuperAttacks;
    }
    public Set<Attack> getUltimates(){
        return this.unlockedUltimates;
    }
    public Set<PassiveAbility> getPassives(){
        return this.unlockedPassives;
    }

    public double getMaxBaseSpirit(){
        return this.maxBaseSpirit;
    }
    public void setMaxBaseSpirit(double max){
        this.maxBaseSpirit = max;
    }
    public void addMaxBaseSpirit(double spirit){
        this.setMaxBaseSpirit(this.getMaxBaseSpirit() + spirit);
    }
    public void removeMaxBaseSpirit(double spirit){
        this.setMaxBaseSpirit(this.getMaxBaseSpirit() - spirit);
    }

    public double getMaxSpirit(){
        return this.getMaxBaseSpirit() * (passiveAbility != null ? passiveAbility.getSpiritBonus() : 1);
    }

    public double getSpirit(){
        if(this.currentSpirit > this.getMaxSpirit())
            return this.getMaxSpirit();
        return this.currentSpirit;
    }
    public void setSpirit(double spirit){
        if(spirit <= 0) {
            spirit = 0;
        }
        if(spirit > this.getMaxSpirit())
            spirit = this.getMaxSpirit();

        if(this.getSpirit() == spirit)
            return;

        this.currentSpirit = spirit;
        this.tellPlayerAboutGaugeUpdate();
    }

    /**
     * Adds spirit while taking into account the passive fill modifier
     *
     * Does not add anything if player is fatigued
     * @param spirit Amount of spirit to add
     */
    public void addSpirit(double spirit){
        if(this.isFatigued())
            return;
        this.addSpiritAbsolute(spirit * (this.passiveAbility != null ? this.passiveAbility.getSpiritFillModifier() : 1));
    }

    /**
     * Removes spirit while taking into the passive cost modifier
     * @param spirit
     */
    public void removeSpirit(double spirit){
        this.removeSpiritAbsolute(spirit * (this.passiveAbility != null ? this.passiveAbility.getCostModifier() : 1));
    }

    /**
     * Adds spirit while ignoring passive modifiers
     * @param spirit
     */
    public void addSpiritAbsolute(double spirit){
        this.setSpirit(this.getSpirit() + spirit);
    }

    /**
     * Removes spirit while ignoring passive modifiers
     * @param spirit
     */
    public void removeSpiritAbsolute(double spirit){
        this.setSpirit(this.getSpirit() - spirit);
    }

    private void tellPlayerAboutGaugeUpdate() {
        double gauge = this.getSpirit();
        double cap = this.getMaxSpirit();
        double currPercent = (gauge / cap) * 100; // For gauge display

        if((currPercent - this.lastPercentPrinted) >= 5){
            this.addChatMessage(this.drawPrettyGauge(true));
            //Sets it to the closest lowest value divisible by 5;
            this.lastPercentPrinted = (byte) (currPercent - currPercent%5);
        }

        //If gauge was lowered or lastPercentPrinted wasn't initialized yet
        if(currPercent < this.lastPercentPrinted || this.lastPercentPrinted == 0){
            this.lastPercentPrinted = (byte) (currPercent - currPercent%5);
        }

    }

    public String drawSpiritGauge(){
        double gauge = getSpirit();
        double cap = getMaxSpirit();
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

    public IChatComponent drawPrettyGauge(){
        return this.drawPrettyGauge(false);
    }

    /**
     * @param round Should it ~~floor~~ round it to the closest % divisible by 5
     * @return A Chat component containing the prettified gauge!
     */
    private IChatComponent drawPrettyGauge(boolean round){
        double gauge = this.getSpirit();
        double cap = this.getMaxSpirit();
        double percent = (gauge / cap) * 100;
        if(round)
            percent = (percent - percent%5);
        String formattedPercent = new DecimalFormat("#.##").format(percent);

        StringBuilder gaugeString = new StringBuilder(this.drawSpiritGauge());

        //Colors the filled spirit gauge to aqua.
        int firstIndex;
        firstIndex = gaugeString.indexOf("=");
        if(firstIndex != -1) {
            gaugeString.insert(firstIndex, "<aqua>");
            gaugeString.insert(gaugeString.lastIndexOf("=") + 1, "</aqua>");
        }

        return MiniMessageParser.getFormat("<aqua>==><dark_aqua> <gray><gauge></gray> Your spirit gauge is at <aqua><percent>%</aqua> capacity.", "gauge", gaugeString.toString(), "percent", formattedPercent);
    }

    public boolean hasAbility(Ability ability){
        return unlockedSuperAttacks.contains(ability) || unlockedUltimates.contains(ability) || unlockedPassives.contains(ability);
    }


    public Ability getAbilityFromSlot(String slotName){
        slotName = slotName.toUpperCase();

        switch(slotName){
            case "SUPER1":
                return this.superAttack1;
            case "SUPER2":
                return this.superAttack2;
            case "ULTIMATE":
                return this.ultimateAttack;
            case "PASSIVE":
                return this.passiveAbility;
            default:
                return null;
        }
    }

    public void setAbilityAtSlot(Ability ability, String slotName){

        if(ability instanceof PassiveAbility && slotName.equalsIgnoreCase("passive")){
            this.selectPassive((PassiveAbility) ability);
        }

        if(!(ability instanceof Attack))
            return;

        Attack attack = (Attack) ability;
        switch (slotName.toUpperCase()){
            case "ULTIMATE":
                this.selectUltimateAttack(attack);
            case "SUPER1":
            case "SUPER2":
                this.selectSuperAttack(attack, slotName);
        }
    }

    private void selectSuperAttack(Attack attack, String slot){
        if(attack.isUltimate())
            return;

        if(slot.equalsIgnoreCase("super1")){
            this.superAttack1 = attack;
            this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Super1: <aqua>"+attack));
        }
        if(slot.equalsIgnoreCase("super2")){
            this.superAttack2 = attack;
            this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Super2: <aqua>"+attack));
        }

    }

    private void selectUltimateAttack(Attack attack){
        if(!attack.isUltimate())
            return;
        this.ultimateAttack = attack;
        this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Ultimate: <aqua>"+attack));
    }
    private void selectPassive(PassiveAbility passive){
        this.passiveAbility = passive;
        this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Passive: <aqua>"+passive));
    }

    public void addAbility(Ability ability){
        if(!AbilityDatabase.isRegistered(ability))
            return;

        if(ability instanceof Attack)
            this.addAttack((Attack) ability);

        if(ability instanceof PassiveAbility)
            this.addPassive((PassiveAbility) ability);
    }
    public void removeAbility(Ability ability){
        if(AbilityDatabase.isDefault(ability))
            return;

        if(ability instanceof Attack)
            this.removeAttack((Attack) ability);

        if(ability instanceof PassiveAbility)
            this.removePassive((PassiveAbility) ability);
    }

    private void addAttack(Attack attack){
        if(attack.isUltimate())
            this.addUltimate(attack);
        else
            this.addSuperAttack(attack);
    }
    private void removeAttack(Attack attack) {
        if(attack.isUltimate())
            this.removeUltimate(attack);
        else
            this.removeSuperAttack(attack);
    }

    private void addUltimate(Attack ultimateAttack){
        this.unlockedUltimates.add(ultimateAttack);
    }
    private void removeUltimate(Attack ultimateAttack){
        this.unlockedUltimates.remove(ultimateAttack);
    }

    private void addSuperAttack(Attack superAttack){
        this.unlockedSuperAttacks.add(superAttack);
    }
    private void removeSuperAttack(Attack superAttack){
        this.unlockedSuperAttacks.remove(superAttack);
    }

    public void addPassive(PassiveAbility passive){
        this.unlockedPassives.add(passive);
    }
    public void removePassive(PassiveAbility passive){
        this.unlockedPassives.remove(passive);
    }

    public boolean isFatigued(){
        return dbcPlayer.isFatigued();
    }
}
