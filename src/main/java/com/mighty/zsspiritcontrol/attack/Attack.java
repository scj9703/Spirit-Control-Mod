package com.mighty.zsspiritcontrol.attack;

/** Super and Ultimate Attacks **/
public class Attack {
    // Identifiable attack name for command use
    String name;
    // Type of Ki attack (wave, blast, etc)
    int type;
    // Color of Ki attack
    int color;
    // Speed of attack
    int speed;
    // Whether effect is on
    int effect;
    // Damage modifier of Attack
    double dmgModifier;
    // Gauge cost of attack
    double cost;
    // Casttime (in seconds?) of attack
    double casttime;
    // Displays in chat when firing
    String fireMessage;
    // Description displayed when viewed in /sc
    String desc;
    // Whether this attack is an Ultimate
    boolean isUltimate;
    // How much fatigue (in minutes) the attack gives (Supers = 0)
    double fatigue;

    // Constructor
    public Attack(String name, int type, int color, int speed, int effect, double dmgModifier,
                  double cost, double casttime, String fireMessage, String desc, boolean isUltimate, double fatigue) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.speed = speed;
        this.effect = effect;
        this.dmgModifier = dmgModifier;
        this.cost = cost;
        this.casttime = casttime;
        this.fireMessage = fireMessage;
        this.desc = desc;
        this.isUltimate = isUltimate;
        this.fatigue = fatigue;
    }

    public String getName() {
        return name;
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

    public String getDesc() {
        return desc;
    }

    public boolean isUltimate() {
        return isUltimate;
    }

    public double getFatigue() {
        return fatigue;
    }
}
