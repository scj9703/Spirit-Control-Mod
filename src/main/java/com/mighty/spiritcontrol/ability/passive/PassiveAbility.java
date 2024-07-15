package com.mighty.spiritcontrol.ability.passive;

import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.player.SCPlayer;

import java.util.HashMap;
import java.util.Set;

public class PassiveAbility extends Ability {

    private final HashMap<Byte, Set<Byte>> raceFormMap;
    private final HashMap<EnumFillMethod, Double> fillMethods;
    private final double spiritBonus;
    private final double costModifier;

    //Non public constructor.
    PassiveAbility(String id, String name, String description, double bonusSpirit, HashMap<EnumFillMethod, Double> fillMethods, double spiritUseModifier, HashMap<Byte, Set<Byte>> raceFormMap){
        super(id, name, description);
        this.raceFormMap = raceFormMap;
        this.spiritBonus = bonusSpirit;
        this.fillMethods = fillMethods;
        this.costModifier = spiritUseModifier;
    }

    /**
     * Checks if the player can use current passive with their form and race.
     * @param player SCPlayer reference
     * @return If the player can use the passive ability
     */
    public boolean canPlayerUsePassive(SCPlayer player){
        if(raceFormMap.isEmpty()) //If the set is empty, everyone can use this passive
            return true;

        Set<Byte> formSet = raceFormMap.getOrDefault(player.getRace(), null);

        //If set isn't empty but it doesn't have your race allowed, return false
        if(formSet == null)
            return false;

        //If set isn't empty but has your race allowed (any form), return true
        if(formSet.isEmpty())
            return true;

        //If formSet isn't empty, check if your form is on the list.
        return formSet.contains(player.getForm());
    }

    /**
     * Get the fill modifier for a given method
     * @param method
     * @return Fill modifier
     */
    public double getSpiritFillModifier(EnumFillMethod method) {
        return fillMethods.getOrDefault(method, 0.0);
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
