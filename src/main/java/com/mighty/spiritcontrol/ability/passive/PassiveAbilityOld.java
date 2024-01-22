package com.mighty.spiritcontrol.ability.passive;

import com.mighty.spiritcontrol.ability.Ability;

/** Class for Spirit Control Passive Abilities **/
public class PassiveAbilityOld extends Ability {

    // ID of the race required to activate it (-1 = no req)
    private final int raceId;
    // ID of the form required to activate it (-1 = no req)
    private final int formId;
    // Multiplier for how quickly Spirit Gauge is filled on activation
    private final double spiritFillModifier;
    // Multiplier for how much Capacity the player's Gauge has with this passive.
    private final double spiritBonus;
    // Multiplier for how much Spirit Abilities cost with this passive.
    private final double costModifier;

    public PassiveAbilityOld(String name, int raceId, int formId, double gaugeModifier, double gaugeBonus, double costModifier, String desc) {
        super(name, desc);
        this.raceId = raceId;
        this.formId = formId;
        this.spiritFillModifier = gaugeModifier;
        this.spiritBonus = gaugeBonus;
        this.costModifier = costModifier;
    }

    /**
     * @return the Passive's activation Race Id.
     */
    public int getRaceId() {
        return raceId;
    }

    /**
     * @return the Passive's activation Form Id.
     */
    public int getFormId() {
        return formId;
    }

    /**
     * @return Modifier of how this passive affects spirit gauge refilling.
     */
    public double getSpiritFillModifier() {
        return spiritFillModifier;
    }

    /**
     * @return Amount of bonus spirit.
     */
    public double getSpiritBonus() {
        return spiritBonus;
    }

    /**
     * @return Spirit cost modifier.
     */
    public double getCostModifier() {
        return costModifier;
    }
}
