package com.mighty.zsspiritcontrol.ability.attack;

import com.mighty.zsspiritcontrol.ability.AbilityBuilder;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackColor;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackType;

public class AttackBuilder extends AbilityBuilder {

    protected EnumAttackType type = EnumAttackType.BLAST;
    protected EnumAttackColor color = EnumAttackColor.ALIGNMENT_BASED;
    protected int speed = 1;
    protected int effect = 0;
    protected double dmgModifier = 1;
    protected double cost = 50;
    protected double casttime = 5;
    protected String fireMessage = "Take this!";
    protected boolean isUltimate = false;
    protected double fatigue = 0.0;


    public AttackBuilder setType(EnumAttackType type) {
        this.type = type;
		return this;
    }

    public AttackBuilder setColor(EnumAttackColor color) {
        this.color = color;
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

    public AttackBuilder setFireMessage(String fireMessage) {
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

    public AttackBuilder setName(String name){
        super.setName(name);
        return this;
    }

    public AttackBuilder setId(String literalId){
        super.setId(literalId);
        return this;
    }

    public AttackBuilder setDescription(String description){
        super.setDescription(description);
        return this;
    }

    public Attack getAbility(){
        return null;
        //return new Attack(name, type.getValue(), color.getValue(), speed, effect, dmgModifier, cost, casttime, fireMessage, description, isUltimate, fatigue);
    }
}
