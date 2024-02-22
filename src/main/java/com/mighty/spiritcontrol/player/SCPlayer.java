package com.mighty.spiritcontrol.player;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.passive.EnumFillMethod;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;
import somehussar.minimessage.MMParser;
import somehussar.minimessage.util.Util;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SCPlayer implements IExtendedEntityProperties {

    /**
     * Player reference
     */
    public final EntityPlayer player;
    public DBCPlayerWrapper dbcPlayer;
    public byte lastPrintedCharge = 0;

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
    private Attack selectedSuperAttack1;
    private Attack selectedSuperAttack2;
    private Attack selectedUltimateAttack;
    private PassiveAbility selectedPassiveAbility;

    /**
     * Unlocked abilities
     * <p>
     * Sets do not allow duplicate values.
     */
    private final Set<Attack> unlockedSuperAttacks = new HashSet<>();
    private final Set<Attack> unlockedUltimates = new HashSet<>();
    private final Set<PassiveAbility> unlockedPassives = new HashSet<>();
    private final Set<String> unregisteredAbilities = new HashSet<>();

    /**
     * Has unlocked Spirit Control yet?
     */
    private boolean unlockedSpiritControl = false;

    /**
     * SC Charging variables
     */
    private boolean isArmed = false;
    public boolean isChargingAttack = false;
    public byte currentAttackSlot = 0;
    public long lastTimeGainedSpirit = 0;
    public long lastTimeSneaked = 0;
    public long startedCharging = 0;

    private long nextTimeAbleToCastAttack = 0;
    public byte sneakCount = 0;

    /**
     * Toggles the armed state for the player
     */
    public void toggleIsArmed(){
        isArmed = !isArmed;

        addChatMessage(
                MMParser.getFormat("<aqua>==> <dark_aqua>You are now <state>", "state", (isArmed ? "<green>armed" : "<red>disarmed"))
        );
        lastTimeSneaked = 0;
        sneakCount = 0;
    }

    /**
     * Sets the attack cooldown for the player
     * @param seconds time in seconds
     */
    public void setCooldown(double seconds){
        nextTimeAbleToCastAttack = MinecraftServer.getSystemTimeMillis() + ( (int) (seconds*1000));
        startedCharging = 0;
    }

    /**
     * @return If the player is on cooldown from using attacks
     */
    public boolean isOnCooldown(){
        return MinecraftServer.getSystemTimeMillis() - nextTimeAbleToCastAttack <= 0;
    }

    /**
     * @return Cooldown time in seconds.
     */
    public double getCooldown(){
        return (nextTimeAbleToCastAttack - MinecraftServer.getSystemTimeMillis()) / 1000.0;
    }

    /**
     * @return If the player can use their currently selected attack
     */
    public boolean canUseAttack(){
        boolean isUnableToUseUltDueToUlt = false;
        if(getCurrentSelectedAttack().isUltimate())
            isUnableToUseUltDueToUlt = isFatigued();

        return !isOnCooldown() && hasEnoughSpiritToFire() && !isUnableToUseUltDueToUlt;
    }

    /**
     * @return The currently selected attack
     */
    public Attack getCurrentSelectedAttack(){
        if(currentAttackSlot == 1)
            return selectedSuperAttack2;
        if(currentAttackSlot == 2)
            return selectedUltimateAttack;

        return selectedSuperAttack1;
    }

    /**
     * @return If the player is armed to charge SC attacks
     */
    public boolean isArmed(){
        return isArmed;
    }

    /**
     * A ki attack drains the player according to this formula : MaxBaseSpirit * CostModifier
     *
     * @return if the player has enough spirit to fire selected attack
     */
    public boolean hasEnoughSpiritToFire(){
        return currentSpirit >= ((PassiveAbility) getAbilityFromSlot("passive")).getCostModifier() * getCurrentSelectedAttack().getCostModifier() * getMaxBaseSpirit();
    }

    /**
     * Select an attack to charge
     * @param slot slot from 0 to 2 (super1, super2, ultimate)
     */
    public void setCurrentAttackSlot(int slot){
        if(slot > 2 || slot < 0 || currentAttackSlot == slot)
            return;

        this.currentAttackSlot = (byte) slot;
        addChatMessage(MMParser.getFormat("<aqua>==> <dark_aqua>Selected attack: <aqua><attack_name>", "attack_name", Util.getAbilityHover(getCurrentSelectedAttack())));
    }


    /**
     * Creates a new instance of an SCPlayer
     * @param mcPlayer Player reference
     */
    public SCPlayer(EntityPlayer mcPlayer){
        canReceiveMessages = false;
        this.player = mcPlayer;

        loadDefaultData();
    }

    /**
     * Creates default data for the player
     */
    private void loadDefaultData(){
        this.canReceiveMessages = false;

        this.dbcPlayer = new DBCPlayerWrapper(player);
        Attack kiAttack = (Attack) AbilityDatabase.getDefaultSuper();
        Attack energyWave = (Attack) AbilityDatabase.getDefaultUltimate();
        PassiveAbility virtuousSpirit = (PassiveAbility) AbilityDatabase.getDefaultPassive();

        unlockedPassives.clear();
        unlockedSuperAttacks.clear();
        unlockedUltimates.clear();
        unregisteredAbilities.clear();

        this.addAbility(kiAttack);
        this.addAbility(energyWave);
        this.addAbility(virtuousSpirit);

        this.setAbilityAtSlot(kiAttack, "super1");
        this.setAbilityAtSlot(kiAttack, "super2");
        this.setAbilityAtSlot(energyWave, "ultimate");
        this.setAbilityAtSlot(virtuousSpirit, "passive");

        this.canReceiveMessages = true;
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

        scTag.setByte("selectedAttackSlot", this.currentAttackSlot);

        scTag.setDouble("maxSpirit", this.maxBaseSpirit);
        scTag.setDouble("currentSpirit", this.currentSpirit);

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

        NBTTagList unregistered = new NBTTagList();
        for(String abilityId : unregisteredAbilities){
            unregistered.appendTag(new NBTTagString(abilityId));
        }
        if(unregistered.tagCount() > 0)
            scTag.setTag("UnregisteredAbilities", unregistered);

        compound.setTag("SpiritControl", scTag);

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {

        loadDefaultData();

        if(!compound.hasKey("SpiritControl")){
            return;
        }

        canReceiveMessages = false; //Disables updates messages while loading the player (dimension changes, relogs)

        NBTTagCompound scTag = compound.getCompoundTag("SpiritControl");

        this.setUnlockedSpiritControl(scTag.getBoolean("hasUnlocked"));
        this.isArmed = scTag.getBoolean("isArmed");

        this.currentAttackSlot = scTag.getByte("selectedAttackSlot");

        NBTTagList passivesData = scTag.getTagList("Passives", Constants.NBT.TAG_STRING);
        NBTTagList supersData = scTag.getTagList("Supers", Constants.NBT.TAG_STRING);
        NBTTagList ultimatesData = scTag.getTagList("Ultimates", Constants.NBT.TAG_STRING);
        NBTTagList unregisteredAbilityData = scTag.getTagList("UnregisteredAbilities", Constants.NBT.TAG_STRING);

        loadAbilityData(passivesData);
        loadAbilityData(supersData);
        loadAbilityData(ultimatesData);
        loadAbilityData(unregisteredAbilityData);

        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Super1")), "super1");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Super2")), "super2");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Ultimate")), "ultimate");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityById(scTag.getString("Passive")), "passive");

        this.setMaxBaseSpirit(scTag.getDouble("maxSpirit"));
        if(maxBaseSpirit == 0)
            maxBaseSpirit = 1000;
        this.setSpirit(scTag.getDouble("currentSpirit"));

        this.canReceiveMessages = true;
    }

    /**
     * Load ability data from a NBTTagList
     * @param tagList
     */
    private void loadAbilityData(NBTTagList tagList) {
        for(int i = 0; i < tagList.tagCount(); i++){
            Ability ability = AbilityDatabase.getAbilityById(tagList.getStringTagAt(i));
            if(ability == null){
                this.unregisteredAbilities.add(tagList.getStringTagAt(i));
            }else{
                this.addAbility(ability);
            }
        }
    }

    /**
     * Send a chat message to the player if the player can receive messages.
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
        return this.unlockedSuperAttacks.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * @return a set of ultimate attacks the player unlocked
     */
    public Set<Attack> getUnlockedUltimates(){
        return this.unlockedUltimates.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * @return a set of passive abilities the player unlocked
     */
    public Set<PassiveAbility> getUnlockedPassives(){
        return this.unlockedPassives.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Does NOT include passives
     * @return Players max amount of spirit WITHOUT passive modifiers
     */
    public double getMaxBaseSpirit(){
        return this.maxBaseSpirit;
    }

    /**
     * Sets the max base spirit (without passive modifiers) to specified value
     * @param max new max
     */
    public void setMaxBaseSpirit(double max){
        this.maxBaseSpirit = max;
    }

    /**
     * Raises the max spirit (without passive buffs)
     * @param spirit amount of spirit to add to the cap
     */
    public void addMaxBaseSpirit(double spirit){
        this.setMaxBaseSpirit(this.getMaxBaseSpirit() + spirit);
    }

    /**
     * Lowers the max spirit (without passive buffs)
     * @param spirit amount of spirit to take away from the cap
     */
    public void removeMaxBaseSpirit(double spirit){
        this.setMaxBaseSpirit(this.getMaxBaseSpirit() - spirit);
    }

    /**
     * Includes passives
     * @return Players max amount of spirit WITH passive modifiers
     */
    public double getMaxSpirit(){
        return this.getMaxBaseSpirit() * (selectedPassiveAbility != null ? selectedPassiveAbility.getSpiritBonus() : 1);
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
    public void addSpiritByPassive(double spirit, EnumFillMethod method){
        this.addSpirit(spirit * (this.selectedPassiveAbility != null ? this.selectedPassiveAbility.getSpiritFillModifier(method) : 1));
    }

    /**
     * Removes spirit while taking into the passive cost modifier
     * @param spirit
     */
    public void removeSpiritByPassive(double spirit){
        this.removeSpirit(spirit * (this.selectedPassiveAbility == null ? 1 : this.selectedPassiveAbility.getCostModifier()));
    }

    /**
     * Adds spirit while ignoring passive modifiers
     * @param spirit
     */
    public void addSpirit(double spirit){
        this.setSpirit(this.getSpirit() + spirit);
    }

    /**
     * Removes spirit while ignoring passive modifiers
     * @param spirit
     */
    public void removeSpirit(double spirit){
        this.setSpirit(this.getSpirit() - spirit);
    }

    /**
     * Draws the gauge at least every 5% change
     *
     * Checking if spirit gauge is cleanly divisible by a
     * number does not work due to the nature of decimal numbers
     */
    public void tellPlayerAboutGaugeUpdate() {
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
     * Creates the spirit gauge as an uncolored String
     * @return Spirit gauge string
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
     * @param round Should it floor it to the closest % divisible by 5
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

        return MMParser.getFormat("<aqua>==><dark_aqua> <gray><gauge></gray> Your spirit gauge is at <aqua><percent>%</aqua> capacity.", "gauge", gaugeString.toString(), "percent", formattedPercent);
    }

    /**
     * @param ability ability to check
     * @return If the player has this ability unlocked
     */
    public boolean hasAbility(Ability ability){
        return unlockedSuperAttacks.contains(ability) || unlockedUltimates.contains(ability) || unlockedPassives.contains(ability);
    }

    /**
     * @param abilityId ID of the ability to check
     * @return If the player has this ability unlocked
     */
    public boolean hasAbility(String abilityId){
        return hasAbility(AbilityDatabase.getAbilityById(abilityId));
    }

    /**
     * @param slotName Name of the slot that stores an attack (super1, super2, ultimate, passive)
     * @return Ability stored in the slot
     */
    public Ability
    getAbilityFromSlot(String slotName){
        slotName = slotName.toUpperCase();

        this.updateSelectedAbilities();

        switch(slotName){
            case "SUPER1":
                return this.selectedSuperAttack1;
            case "SUPER2":
                return this.selectedSuperAttack2;
            case "ULTIMATE":
                return this.selectedUltimateAttack;
            case "PASSIVE":
                return this.selectedPassiveAbility;
            default:
                return null;
        }
    }

    /**
     * Equips the ability at a slot (even if they don't have it unlocked);
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

    /**
     * Equips an attack into the player's load-out.
     * @param attack Attack to select
     */
    private void selectSuperAttack(Attack attack, String slot){
        if(attack.isUltimate())
            return;

        if(slot.equalsIgnoreCase("super1")){
            this.selectedSuperAttack1 = attack;
            this.addChatMessage(MMParser.getFormat("<dark_aqua>Equipped Super1: <aqua>"+Util.getAbilityHover(attack)));
        }
        if(slot.equalsIgnoreCase("super2")){
            this.selectedSuperAttack2 = attack;
            this.addChatMessage(MMParser.getFormat("<dark_aqua>Equipped Super2: <aqua>"+Util.getAbilityHover(attack)));
        }

    }

    /**
     * Equips an ultimate into the player's load-out.
     * @param attack Ultimate to select
     */
    private void selectUltimateAttack(Attack attack){
        if(!attack.isUltimate())
            return;
        this.selectedUltimateAttack = attack;
        this.addChatMessage(MMParser.getFormat("<dark_aqua>Equipped Ultimate: <aqua>"+Util.getAbilityHover(attack)));
    }

    /**
     * Equips an passive into the player's load-out.
     * @param passive Passive to select
     */
    private void selectPassive(PassiveAbility passive){
        this.selectedPassiveAbility = passive;
        this.addChatMessage(MMParser.getFormat("<dark_aqua>Equipped Passive: <aqua>"+Util.getAbilityHover(passive)));
    }

    public void addAbility(String abilityID){
        addAbility(AbilityDatabase.getAbilityById(abilityID));
    }

    /**
     * Adds an ability to the player's skill set
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

    /**
     * Adds an attack to the player's skill set
     * @param attack The attack to add
     */
    public void addAttack(Attack attack){
        if(attack.isUltimate())
            this.unlockedUltimates.add(attack);
        else
            this.unlockedSuperAttacks.add(attack);
    }

    /**
     * Removes an attack from the player's skill set
     * @param attack the attack to remove
     */
    private void removeAttack(Attack attack) {
        if(AbilityDatabase.isDefault(attack))
            return;

        if(attack.isUltimate())
            this.unlockedUltimates.remove(attack);
        else
            this.unlockedSuperAttacks.remove(attack);

        this.updateSelectedAbilities();
    }

    /**
     * Adds a passive ability to the player's skill set
     * @param passive the passive to add
     */
    private void addPassive(PassiveAbility passive){
        this.unlockedPassives.add(passive);
    }

    /**
     * Removes a passive ability from the player's skill set
     * @param passive the passive to remove
     */
    private void removePassive(PassiveAbility passive){
        if(AbilityDatabase.isDefault(passive))
            return;

        this.unlockedPassives.remove(passive);

        this.updateSelectedAbilities();
    }

    /**
     * Updates the player's load-out to potentially remove abilities they lost access to
     */
    private void updateSelectedAbilities() {
        if(!this.hasAbility(this.selectedPassiveAbility))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultPassive(), "passive");

        if(!this.hasAbility(this.selectedSuperAttack1))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultSuper(), "super1");

        if(!this.hasAbility(this.selectedSuperAttack2))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultSuper(), "super2");

        if(!this.hasAbility(this.selectedUltimateAttack))
            this.setAbilityAtSlot(AbilityDatabase.getDefaultUltimate(), "ultimate");
    }

    /**
     * @return Is the player DBC Fatigued
     */
    public boolean isFatigued(){
        return dbcPlayer.isFatigued();
    }

    /**
     * @return The player's current form
     */
    public byte getForm() {
        //return new DBCPlayerHelper(player).getState();
        return dbcPlayer.getForm();
    }

    /**
     * @return The player's race
     */
    public byte getRace(){
        return dbcPlayer.getRace();
    }

    /**
     * @return Is the player using the C button to charge
     */
    public boolean isChargingDBC(){
        return dbcPlayer.isCharging();
    }

    /**
     * @param method type of method
     * @return True if a player can fill their gauge using this method
     */
    public boolean canPlayerUsePassive(EnumFillMethod method) {
        if(selectedPassiveAbility == null)
            return false;
        return selectedPassiveAbility.canPlayerUsePassive(this);
    }

    /**
     * Sets the player's fatigue
     * @param fatigue in minutes
     */
    public void setFatigue(double fatigue) {
        dbcPlayer.setFatigue(fatigue);
    }
}
