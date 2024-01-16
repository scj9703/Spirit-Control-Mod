package com.mighty.zsspiritcontrol.attack;

/** Super and Ultimate Attacks **/
public class Attack extends Ability {
    // Type of Ki attack (wave, blast, etc)
    private final int type;
    // Color of Ki attack
    private final int color;
    // Speed of attack
    private final int speed;
    // Whether effect is on
    private final int effect;
    // Damage modifier of Attack
    private final double dmgModifier;
    // Gauge cost of attack
    private final double cost;
    // Casttime (in seconds?) of attack
    private final double casttime;
    // Displays in chat when firing
    private final String fireMessage;
    // Description displayed when viewed in /sc
    private final boolean isUltimate;
    // How much fatigue (in minutes) the attack gives (Supers = 0)
    private final double fatigue;

    Attack(String name, int type, int color, int speed, int effect, double dmgModifier,
                  double cost, double casttime, String fireMessage, String desc, boolean isUltimate, double fatigue) {
        super(name, desc);
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

    public int getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public int getEffect() {
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

    public String getFireMessage() {
        return fireMessage;
    }

    public boolean isUltimate() {
        return isUltimate;
    }

    public double getFatigue() {
        return fatigue;
    }
}
