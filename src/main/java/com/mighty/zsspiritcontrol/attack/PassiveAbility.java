package com.mighty.zsspiritcontrol.attack;

/** Class for Spirit Control Passive Abilities **/
public class PassiveAbility {
    // Unique name used to identify ability
    String name;
    // ID of the race required to activate it (-1 = no req)
    int raceId;
    // ID of the form required to activate it (-1 = no req)
    int formId;
    // Multiplier for how quickly Spirit Gauge is filled on activation
    double gaugeModifier;
    // Multiplier for how much Capacity the player's Gauge has with this passive.
    double gaugeBonus;
    // Multiplier for how much Spirit Abilities cost with this passive.
    double costModifier;
    // Description used for display to players.
    String desc;

    // Constructor
    public PassiveAbility(String name, int raceId, int formId, double gaugeModifier, double gaugeBonus, double costModifier, String desc) {
        this.name = name;
        this.raceId = raceId;
        this.formId = formId;
        this.gaugeModifier = gaugeModifier;
        this.gaugeBonus = gaugeBonus;
        this.costModifier = costModifier;
        this.desc = desc;
    }

    // Returns the Passive's identifiable name.
    public String getName() {
        return name;
    }
    // Returns the Passive's activation Race ID.
    public int getRaceId() {
        return raceId;
    }
    // Returns the Passive's activation Form ID.
    public int getFormId() {
        return formId;
    }
    // Returns the Gauge filling modifier.
    public double getGaugeModifier() {
        return gaugeModifier;
    }
    // Returns the Gauge Capacity Bonus.
    public double getGaugeBonus() {
        return gaugeBonus;
    }
    // Returns the Ability Cost Modifier
    public double getCostModifier() {
        return costModifier;
    }
    // Returns the Passive's Description.
    public String getDesc() {
        return desc;
    }
}
