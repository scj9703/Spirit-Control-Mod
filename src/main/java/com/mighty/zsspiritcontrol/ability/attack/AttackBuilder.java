package com.mighty.zsspiritcontrol.ability.attack;

import com.mighty.zsspiritcontrol.ability.AbilityBuilder;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackColor;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackType;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import somehussar.minimessage.MiniMessageParser;

public class AttackBuilder extends AbilityBuilder {

    protected byte type = (byte) EnumAttackType.BLAST.getValue();
    protected byte color = (byte) EnumAttackColor.ALIGNMENT_BASED.getValue();
    protected int speed = 5;
    protected boolean effect = false;
    protected double dmgModifier = 1;
    protected double cost = 50;
    protected double casttime = 5;
    protected IChatComponent prettyFireMessage;
    protected String fireMessage = "TAKE THIS!";
    protected boolean isUltimate = false;
    protected double fatigue = 0.0;


    public AttackBuilder setType(EnumAttackType type) {
        this.type = (byte) type.getValue();
		return this;
    }

    public AttackBuilder setColor(EnumAttackColor color) {
        this.color = (byte) color.getValue();
		return this;
    }

    public AttackBuilder setSpeed(int speed) {
        this.speed = speed;
		return this;
    }

    public AttackBuilder setEffect(boolean effect) {
        this.effect = effect;
		return this;
    }

    public AttackBuilder setDmgModifier(double dmgModifier) {
        this.dmgModifier = dmgModifier;
		return this;
    }

    public AttackBuilder setCost(double cost) {
        this.cost = cost;
		return this;
    }

    public AttackBuilder setCasttime(double casttime) {
        this.casttime = casttime;
		return this;
    }

    public AttackBuilder setPrettyFireMessage(IChatComponent prettyFireMessage) {
        this.prettyFireMessage = prettyFireMessage;
		return this;
    }

    public AttackBuilder setPrettyFireMessage(String prettyFireMessage) {
        this.prettyFireMessage = MiniMessageParser.getFormat(prettyFireMessage);
        return this;
    }

    public AttackBuilder setFireMessage(String fireMessage){
        this.fireMessage = fireMessage;
        return this;
    }

    public AttackBuilder setUltimate(boolean ultimate) {
        isUltimate = ultimate;
		return this;
    }

    public AttackBuilder setFatigue(double fatigue) {
        this.fatigue = fatigue;
		return this;
    }

    public AttackBuilder setName(String prettyName){
        super.setName(prettyName);
        return this;
    }
    public AttackBuilder setPrettyName(IChatComponent name){
        super.setPrettyName(name);
        return this;
    }
    public AttackBuilder setPrettyName(String name){
        super.setPrettyName(name);
        return this;
    }

    public AttackBuilder setId(String literalId){
        super.setId(literalId);
        return this;
    }

    public AttackBuilder setDescription(String prettyDescription){
        super.setDescription(prettyDescription);
        return this;
    }
    public AttackBuilder setPrettyDescription(IChatComponent description){
        super.setPrettyDescription(description);
        return this;
    }
    public AttackBuilder setPrettyDescription(String description){
        super.setPrettyDescription(description);
        return this;
    }

    public Attack getAbility(){
        super.getAbility();
        if(this.prettyFireMessage == null)
            this.prettyFireMessage = new ChatComponentText(this.fireMessage);
        return new Attack(literalId, prettyName, prettyDescription, type, color, prettyFireMessage, speed, effect, dmgModifier, cost, casttime, isUltimate, fatigue);
        //return new Attack(name, type.getValue(), color.getValue(), speed, effect, dmgModifier, cost, casttime, fireMessage, description, isUltimate, fatigue);
    }
}
