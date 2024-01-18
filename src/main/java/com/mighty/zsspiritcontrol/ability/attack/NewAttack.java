package com.mighty.zsspiritcontrol.ability.attack;

import com.mighty.zsspiritcontrol.ability.Ability;
import net.minecraft.util.IChatComponent;

public class NewAttack extends Ability {

    private final byte type;
    private final byte color;
    private final int speed;
    private final boolean effect;
    private final double dmgModifier;
    private final double cost;
    private final double casttime;
    private final IChatComponent fireMessage;
    private final boolean isUltimate;
    private final double fatigue;

    NewAttack(String id, String name, String description, byte type, byte color, IChatComponent fireMessage, int speed, boolean effect, double dmgModifier, double cost, double casttime, boolean isUltimate, double fatigue){
        super(id, name, description);
        this.type = type;
        this.color = color;
        this.speed = speed;
        this.effect = effect;
        this.dmgModifier = dmgModifier;
        this.cost = cost;
        this.casttime = casttime;
        this.fireMessage = fireMessage;
        this.isUltimate = isUltimate;
        this.fatigue = fatigue;

    }

    public byte getType() {
        return type;
    }

    public byte getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isEffect() {
        return effect;
    }

    public double getDmgModifier() {
        return dmgModifier;
    }

    public double getCost() {
        return cost;
    }

    public double getCasttime() {
        return casttime;
    }

    public IChatComponent getFireMessage() {
        return fireMessage;
    }

    public boolean isUltimate() {
        return isUltimate;
    }

    public double getFatigue() {
        return fatigue;
    }
}
