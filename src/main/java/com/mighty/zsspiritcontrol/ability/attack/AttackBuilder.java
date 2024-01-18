package com.mighty.zsspiritcontrol.ability.attack;

import com.mighty.zsspiritcontrol.ability.AbilityBuilder;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackColor;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackType;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class AttackBuilder extends AbilityBuilder {

    protected byte type = (byte) EnumAttackType.BLAST.getValue();
    protected byte color = (byte) EnumAttackColor.ALIGNMENT_BASED.getValue();
    protected int speed = 1;
    protected int effect = 0;
    protected double dmgModifier = 1;
    protected double cost = 50;
    protected double casttime = 5;
    protected IChatComponent fireMessage = new ChatComponentText("Take this!");
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

    public AttackBuilder setEffect(int effect) {
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

    public AttackBuilder setFireMessage(IChatComponent fireMessage) {
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

    public AttackBuilder setName(IChatComponent name){
        super.setName(name);
        return this;
    }

    public AttackBuilder setId(String literalId){
        super.setId(literalId);
        return this;
    }

    public AttackBuilder setDescription(IChatComponent description){
        super.setDescription(description);
        return this;
    }

    public Attack getAbility(){
        return null;
        //return new Attack(name, type.getValue(), color.getValue(), speed, effect, dmgModifier, cost, casttime, fireMessage, description, isUltimate, fatigue);
    }
}
