package com.mighty.zsspiritcontrol.ability.passive;

import com.mighty.zsspiritcontrol.ability.Ability;
import com.mighty.zsspiritcontrol.player.SCPlayer;

import java.util.HashMap;
import java.util.Set;

public class NewPassiveAbility extends Ability {

    private final HashMap<Byte, Set<Byte>> raceFormMap;
    private final double spiritFillModifier;
    private final double spiritBonus;
    private final double costModifier;
    private final Set<EnumFillMethod> fillMethods;

    //Non public constructor.
    NewPassiveAbility(String id, String name, String description, double bonusSpirit, double spiritFillModifier, double spiritUseModifier, HashMap<Byte, Set<Byte>> raceFormMap, Set<EnumFillMethod> fillMethods){
        super(id, name, description);
        this.raceFormMap = raceFormMap;
        this.spiritBonus = bonusSpirit;
        this.spiritFillModifier = spiritFillModifier;
        this.costModifier = spiritUseModifier;
        this.fillMethods = fillMethods;
    }

    /**
     * Checks if the player can use current passive with their form and race.
     * @param player SCPlayer reference
     * @return If the player can use the passive ability
     */
    public boolean canPlayerUsePassive(SCPlayer player){
        if(raceFormMap.isEmpty()) //If the hashmap is empty, everyone can use this passive
            return true;

        Set<Byte> formSet = raceFormMap.getOrDefault(player.getRace(), null);

        //If hashmap isn't empty but it doesn't have your race allowed, return false
        if(formSet == null)
            return false;

        //If hashmap isn't empty but has your race allowed (any form), return true
        if(formSet.isEmpty())
            return true;

        //If formSet isn't empty, check if your form is on the list.
        return formSet.contains(player.getForm());
    }

    /**
     * Checks if a passive can be filled by this method
     * @param fillMethod type of method you'd like to check
     * @return True or false
     */
    public boolean canPassiveFillLikeThis(EnumFillMethod fillMethod){
        return this.fillMethods.contains(fillMethod);
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
