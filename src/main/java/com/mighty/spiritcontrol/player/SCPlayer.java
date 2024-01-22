package com.mighty.spiritcontrol.player;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.EnumFillMethod;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import kamkeel.zslib.util.dbc.DBCPlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;
import somehussar.minimessage.MiniMessageParser;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class SCPlayer implements IExtendedEntityProperties {

    /**
     * Player reference
     */
    public final EntityPlayer player;

    /**
     * DBCPlayerWrapper.
     *
     * Hopefully will move this to be a ZS lib class later on.
     */
    private DBCPlayerHelper dbcPlayer;
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
    private Attack currentSuperAttack1;
    private Attack currentSuperAttack2;
    private Attack currentUltimateAttack;
    private PassiveAbility currentPassiveAbility;

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

    private boolean isArmed = false;
    public boolean isCharging = false;
    private byte currentAttackSlot = 0;
    public long lastTimeSwinged = 0;
    public long lastTimeSneaked = 0;
    public byte sneakCount = 0;
    public void toggleIsArmed(){
        isArmed = !isArmed;

        addChatMessage(new ChatComponentText("You are now " + (isArmed ? "armed" : "disarmed") + "."));
        lastTimeSneaked = 0;
        sneakCount = 0;
    }

    public boolean isArmed(){
        return isArmed;
    }

    public void setCurrentAttackSlot(int slot){
        if(slot > 2 || slot < 0)
            return;

        this.currentAttackSlot = (byte) slot;
        switch(slot){
            case 0:
                this.addChatMessage(new ChatComponentText("You just selected super 1"));
                break;
            case 1:
                this.addChatMessage(new ChatComponentText("You just selected super 2"));
                break;
            case 2:
                this.addChatMessage(new ChatComponentText("You just selected ultimate!"));
                break;
        }
    }


    public SCPlayer(EntityPlayer mcPlayer){
        canReceiveMessages = false;
        this.player = mcPlayer;
        this.dbcPlayer = new DBCPlayerHelper(mcPlayer);

        Attack kiAttack = (Attack) AbilityDatabase.getDefaultSuper();
        Attack energyWave = (Attack) AbilityDatabase.getDefaultUltimate();
        PassiveAbility virtuousSpirit = (PassiveAbility) AbilityDatabase.getDefaultPassive();

        this.addAbility(kiAttack);
        this.addAbility(energyWave);
        this.addAbility(virtuousSpirit);

        this.setAbilityAtSlot(kiAttack, "super1");
        this.setAbilityAtSlot(kiAttack, "super2");
        this.setAbilityAtSlot(energyWave, "ultimate");
        this.setAbilityAtSlot(virtuousSpirit, "passive");

    }

    /**
     * Get the extended player instance from player.
     * @param player player which to extend
     * @return a new instance of SCPlayer wrapping the inputted player
     */
    public static SCPlayer getPlayer(EntityPlayer player){
        return (SCPlayer) player.getExtendedProperties(SpiritControl.MODID);
    }

    /**
     * Register a player entity for Spirit Control
     * @param player player which to register
     */
    public static void register(EntityPlayer player){
        if(getPlayer(player) == null)
            player.registerExtendedProperties(SpiritControl.MODID, new SCPlayer(player));
    }

    /**
     * Copy the Spirit Control values of that player.
     * @param otherPlayer
     */
    public void copy(SCPlayer otherPlayer){
        NBTTagCompound nbt = new NBTTagCompound();
        otherPlayer.saveNBTData(nbt);
        this.loadNBTData(nbt);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound scTag = new NBTTagCompound();

        scTag.setBoolean("hasUnlocked", this.hasUnlockedSpiritControl());
        scTag.setBoolean("isArmed", this.isArmed);

        scTag.setDouble("maxSpirit", this.getMaxBaseSpirit());
        scTag.setDouble("currentSpirit", this.getSpirit());

        scTag.setString("Super1", this.getAbilityFromSlot("Super1").getId());
        scTag.setString("Super2", this.getAbilityFromSlot("Super2").getId());
        scTag.setString("Ultimate", this.getAbilityFromSlot("Ultimate").getId());
        scTag.setString("Passive", this.getAbilityFromSlot("Passive").getId());

        NBTTagList superList = new NBTTagList();
        for(Attack att : this.getUnlockedSuperAttacks())
            superList.appendTag(new NBTTagString(att.getId()));
        scTag.setTag("Supers", superList);

        NBTTagList ultimateList = new NBTTagList();
        for(Attack att : this.getUnlockedUltimates())
            ultimateList.appendTag(new NBTTagString(att.getId()));
        scTag.setTag("Ultimates", ultimateList);

        NBTTagList passiveList = new NBTTagList();
        for(PassiveAbility passive : this.getUnlockedPassives())
            passiveList.appendTag(new NBTTagString(passive.getId()));
        scTag.setTag("Passives", passiveList);

        compound.setTag("SpiritControl", scTag);

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if(!compound.hasKey("SpiritControl")){
            return;
        }

        unlockedPassives.clear();
        unlockedSuperAttacks.clear();
        unlockedUltimates.clear();

        this.dbcPlayer = new DBCPlayerHelper(player);
        canReceiveMessages = false; //Disables updates messages while loading the player (dimension changes, relogs)

        NBTTagCompound scTag = compound.getCompoundTag("SpiritControl");

        this.setUnlockedSpiritControl(scTag.getBoolean("hasUnlocked"));
        this.isArmed = scTag.getBoolean("isArmed");

        NBTTagList passivesData = scTag.getTagList("Passives", Constants.NBT.TAG_STRING);
        NBTTagList supersData = scTag.getTagList("Supers", Constants.NBT.TAG_STRING);
        NBTTagList ultimatesData = scTag.getTagList("Ultimates", Constants.NBT.TAG_STRING);

        for(int i = 0; i < passivesData.tagCount(); i++){
            this.addAbility(AbilityDatabase.getAbilityById(passivesData.getStringTagAt(i)));
        }
        for(int i = 0; i < supersData.tagCount(); i++){
            this.addAbility(AbilityDatabase.getAbilityById(supersData.getStringTagAt(i)));
        }
        for(int i = 0; i < ultimatesData.tagCount(); i++){
            this.addAbility(AbilityDatabase.getAbilityById(ultimatesData.getStringTagAt(i)));
        }

        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Super1")), "super1");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Super2")), "super2");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Ultimate")), "ultimate");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Passive")), "passive");

        this.setMaxBaseSpirit(scTag.getDouble("maxSpirit"));
        this.setSpirit(scTag.getDouble("currentSpirit"));

        this.canReceiveMessages = true;
    }

    /**
     * Send a chat message to the player.
     * @param chatComponent
     */
    public void addChatMessage(IChatComponent chatComponent){
        if(this.canReceiveMessages) // true unless the player is being reloaded or still joining.
            player.addChatMessage(chatComponent);
    }

    @Override
    public void init(Entity entity, World world) {
    }

    /**
     * Enables or disable Spirit Control capabilities for the player
     * @param shouldUnlock
     */
    public void setUnlockedSpiritControl(boolean shouldUnlock){
        this.unlockedSpiritControl = shouldUnlock;
    }

    /**
     * @return if the player unlocked Spirit Control abilities
     */
    public boolean hasUnlockedSpiritControl(){
        return this.unlockedSpiritControl;
    }

    /**
     * @return a set of super attacks the player unlocked
     */
    public Set<Attack> getUnlockedSuperAttacks(){
        return this.unlockedSuperAttacks;
    }

    /**
     * @return a set of ultimate attacks the player unlocked
     */
    public Set<Attack> getUnlockedUltimates(){
        return this.unlockedUltimates;
    }

    /**
     * @return a set of passive abilities the player unlocked
     */
    public Set<PassiveAbility> getUnlockedPassives(){
        return this.unlockedPassives;
    }

    /**
     * Does NOT include passives
     * @return Players max amount of spirit WITHOUT passive modifiers
     */
    private double getMaxBaseSpirit(){
        return this.maxBaseSpirit;
    }

    /**
     * Sets the max base spirit (without passive modifiers) to specified value
     * @param max
     */
    public void setMaxBaseSpirit(double max){
        this.maxBaseSpirit = max;
    }
    public void addMaxBaseSpirit(double spirit){
        this.setMaxBaseSpirit(this.getMaxBaseSpirit() + spirit);
    }
    public void removeMaxBaseSpirit(double spirit){
        this.setMaxBaseSpirit(this.getMaxBaseSpirit() - spirit);
    }

    /**
     * Includes passives
     * @return Players max amount of spirit WITH passive modifiers
     */
    public double getMaxSpirit(){
        return this.getMaxBaseSpirit() * (currentPassiveAbility != null ? currentPassiveAbility.getSpiritBonus() : 1);
    }

    /**
     * @return Players current spirit value
     */
    public double getSpirit(){
        if(this.currentSpirit > this.getMaxSpirit())
            return this.getMaxSpirit();
        return this.currentSpirit;
    }

    /**
     * Sets the players spirit directly to the specified value
     * @param spirit
     */
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
        this.addSpiritAbsolute(spirit * (this.currentPassiveAbility != null ? this.currentPassiveAbility.getSpiritFillModifier() : 1));
    }

    /**
     * Removes spirit while taking into the passive cost modifier
     * @param spirit
     */
    public void removeSpirit(double spirit){
        this.removeSpiritAbsolute(spirit * (this.currentPassiveAbility != null ? this.currentPassiveAbility.getCostModifier() : 1));
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

    /**
     * Draws the spirit gauge as an uncolored String
     * @return Spirit gauge
     */
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

    /**
     * Creates a color formatted gauge
     * <br><br>
     * Does NOT round the percentile to the nearest 5
     * @return A chat component containing the prettified gauge!
     */
    public IChatComponent drawPrettyGauge(){
        return this.drawPrettyGauge(false);
    }

    /**
     * Creates a color formatted gauge
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

    /**
     * Does the player have this ability unlocked?
     * @param ability
     * @return True or False
     */
    public boolean hasAbility(Ability ability){
        return unlockedSuperAttacks.contains(ability) || unlockedUltimates.contains(ability) || unlockedPassives.contains(ability);
    }

    /**
     * @param slotName Name of the slot that stores an attack (super1, super2, ultimate, passive)
     * @return Ability stored in the slot
     */
    public Ability getAbilityFromSlot(String slotName){
        slotName = slotName.toUpperCase();

        this.updateSelectedAbilities();

        switch(slotName){
            case "SUPER1":
                return this.currentSuperAttack1;
            case "SUPER2":
                return this.currentSuperAttack2;
            case "ULTIMATE":
                return this.currentUltimateAttack;
            case "PASSIVE":
                return this.currentPassiveAbility;
            default:
                return null;
        }
    }

    /**
     * Sets the ability at a slot
     * <br><br>
     * (`super1`, `super2`, `ultimate`, `passive`)
     *
     * @param ability
     * @param slotName
     */
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
            this.currentSuperAttack1 = attack;
            this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Super1: <aqua>"+attack));
        }
        if(slot.equalsIgnoreCase("super2")){
            this.currentSuperAttack2 = attack;
            this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Super2: <aqua>"+attack));
        }

    }

    private void selectUltimateAttack(Attack attack){
        if(!attack.isUltimate())
            return;
        this.currentUltimateAttack = attack;
        this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Ultimate: <aqua>"+attack));
    }
    private void selectPassive(PassiveAbility passive){
        this.currentPassiveAbility = passive;
        this.addChatMessage(MiniMessageParser.getFormat("<dark_aqua>Equipped Passive: <aqua>"+passive));
    }

    /**
     * Adds an ability to the player's unlocked ability list
     * @param ability Passive or Attack
     */
    public void addAbility(Ability ability){
        if(ability == null || !AbilityDatabase.isRegistered(ability))
            return;

        if(ability instanceof Attack)
            this.addAttack((Attack) ability);

        if(ability instanceof PassiveAbility)
            this.addPassive((PassiveAbility) ability);
    }

    /**
     * Removes an ability from the player
     * @param ability Passive or Attack
     */
    public void removeAbility(Ability ability){
        if(AbilityDatabase.isDefault(ability))
            return;

        if(ability instanceof Attack)
            this.removeAttack((Attack) ability);

        if(ability instanceof PassiveAbility)
            this.removePassive((PassiveAbility) ability);
    }

    public void addAttack(Attack attack){
        if(attack.isUltimate())
            this.unlockedUltimates.add(attack);
        else
            this.unlockedSuperAttacks.add(attack);
    }
    private void removeAttack(Attack attack) {
        if(AbilityDatabase.isDefault(attack))
            return;

        if(attack.isUltimate())
            this.unlockedUltimates.remove(attack);
        else
            this.unlockedSuperAttacks.remove(attack);

        this.updateSelectedAbilities();
    }

    private void addPassive(PassiveAbility passive){
        this.unlockedPassives.add(passive);
    }
    private void removePassive(PassiveAbility passive){
        if(AbilityDatabase.isDefault(passive))
            return;

        this.unlockedPassives.remove(passive);

        this.updateSelectedAbilities();
    }

    private void updateSelectedAbilities() {
        if(!this.hasAbility(this.currentPassiveAbility))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultPassive(), "passive");

        if(!this.hasAbility(this.currentSuperAttack1))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultSuper(), "super1");

        if(!this.hasAbility(this.currentSuperAttack2))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultSuper(), "super2");

        if(!this.hasAbility(this.currentUltimateAttack))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultUltimate(), "ultimate");
    }

    public boolean isFatigued(){
        return dbcPlayer.isFatigued();
    }

    public byte getForm() {
        return dbcPlayer.getRace();
    }
    public byte getRace(){
        return dbcPlayer.getRace();
    }

    public boolean canPlayerUsePassive(EnumFillMethod method) {
        if(currentPassiveAbility == null)
            return false;
        return currentPassiveAbility.canPassiveFillLikeThis(method) && currentPassiveAbility.canPlayerUsePassive(this);
    }

}
