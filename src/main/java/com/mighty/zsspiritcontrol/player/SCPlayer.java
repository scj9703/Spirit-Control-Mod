package com.mighty.zsspiritcontrol.player;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.attack.Ability;
import com.mighty.zsspiritcontrol.attack.AbilityDatabase;
import com.mighty.zsspiritcontrol.attack.Attack;
import com.mighty.zsspiritcontrol.attack.PassiveAbility;
import com.mighty.zsspiritcontrol.player.chat.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.HashSet;
import java.util.Set;

public class SCPlayer implements IExtendedEntityProperties {

    /**
     * Player reference
     */
    private final EntityPlayer player;

    /**
     * Gauge info
     */
    private double maxSpirit = 1000;
    private double currentSpirit = 0;

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

    /**
     * Needed to check if the player has finished loading into the game before attempting to send messages.
     * Otherwise, the game crashes.
     */
    private boolean canReceiveMessages;

    public SCPlayer(EntityPlayer player){
        canReceiveMessages = false;
        this.player = player;

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

        scTag.setDouble("maxSpirit", this.getMaxSpirit());
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

        NBTTagCompound scTag = compound.getCompoundTag("SpiritControl");

        this.setUnlockedSpiritControl(scTag.getBoolean("hasUnlocked"));

        this.setMaxSpirit(scTag.getDouble("maxSpirit"));
        this.setSpirit(scTag.getDouble("currentSpirit"));

        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Super1")), "super1");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Super2")), "super2");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Ultimate")), "ultimate");
        this.setAbilityAtSlot(AbilityDatabase.getAbilityByName(scTag.getString("Passive")), "passive");

        this.canReceiveMessages = true;
    }

    @Override
    public void init(Entity entity, World world) {
    }

    private boolean canReceiveMessages() {
        return this.canReceiveMessages;
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

    public double getMaxSpirit(){
        return this.maxSpirit;
    }
    public void setMaxSpirit(double max){
        this.maxSpirit = max;
    }

    public double getSpirit(){
        return this.currentSpirit;
    }
    public void setSpirit(double spirit){
        if(spirit < 0)
            spirit = 0;
        if(spirit > this.getMaxSpirit())
            spirit = this.getMaxSpirit();

        this.currentSpirit = spirit;
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
            if(this.canReceiveMessages())
                player.addChatMessage(ChatUtil.getMessage("Equipped Super1: "+attack.getName(), EnumChatFormatting.DARK_AQUA));
        }
        if(slot.equalsIgnoreCase("super2")){
            this.superAttack2 = attack;
            if(this.canReceiveMessages())
                player.addChatMessage(ChatUtil.getMessage("Equipped Super2: "+attack.getName(), EnumChatFormatting.DARK_AQUA));
        }

    }

    private void selectUltimateAttack(Attack attack){
        if(!attack.isUltimate())
            return;
        this.ultimateAttack = attack;
        if(this.canReceiveMessages())
            player.addChatMessage(ChatUtil.getMessage("Equipped Ultimate: "+attack.getName(), EnumChatFormatting.DARK_AQUA));
    }
    private void selectPassive(PassiveAbility passive){
        this.passiveAbility = passive;
        if(this.canReceiveMessages())
            player.addChatMessage(ChatUtil.getMessage("Equipped Passive: "+passive.getName(), EnumChatFormatting.DARK_AQUA));
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
}
