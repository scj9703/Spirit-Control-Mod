package com.mighty.spiritcontrol.ability.attack;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.AbilityBuilder;

public class AttackBuilder extends AbilityBuilder {

    protected EnumAttackType type = EnumAttackType.BLAST;
    protected EnumAttackColor color = EnumAttackColor.ALIGNMENT_BASED;
    protected int speed = 5;
    protected int density = 1;
    protected boolean effect = false;
    protected double dmgModifier = 1;
    protected double cost = 50;
    protected double casttime = 5;

    protected double cooldown = 5;
    protected String fireMessage = "<italic><red>Take this!";
    protected boolean isUltimate = false;
    protected double fatigue = 0.0;


    public AttackBuilder setType(EnumAttackType type) {
        if(type != null)
            this.type = type;
		return this;
    }

    public AttackBuilder setType(String type){
        try{
            this.setType(EnumAttackType.valueOf(type.toUpperCase()));
        } catch(Exception e){
            SpiritControl.LOGGER.warn("Can't find color '"+type+"'. ", e);
        }
        return this;
    }

    public AttackBuilder setColor(EnumAttackColor color) {
        if(color != null)
            this.color = color;
		return this;
    }

    public AttackBuilder setColor(String color){
        try{
            this.setColor(EnumAttackColor.valueOf(color.toUpperCase()));
        } catch(Exception e){
            SpiritControl.LOGGER.warn("Can't find color '"+color+"'. ", e);
        }
        return this;
    }

    public AttackBuilder setSpeed(int speed) {
        this.speed = speed;
		return this;
    }

    public AttackBuilder setDensity(int density){
        this.density = density;
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

    public AttackBuilder setFireMessage(String fireMessage){
        if(fireMessage != null)
            this.fireMessage = fireMessage.replace("&", "\u00a7");
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

    public AttackBuilder setDescription(String prettyDescription){
        super.setDescription(prettyDescription);
        return this;
    }

    public Attack getAbility(){
        return new Attack(literalId, name, description, (byte) type.getValue(), (byte) color.getValue(), fireMessage, speed, density, effect, dmgModifier, cost, cooldown, casttime, isUltimate, fatigue);
        //return new Attack(name, type.getValue(), color.getValue(), speed, effect, dmgModifier, cost, casttime, fireMessage, description, isUltimate, fatigue);
    }

    public AttackBuilder setCooldown(double cooldown) {
        this.cooldown = cooldown;
        return this;
    }
}
