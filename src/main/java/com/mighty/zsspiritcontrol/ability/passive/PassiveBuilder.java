package com.mighty.zsspiritcontrol.ability.passive;

import com.mighty.zsspiritcontrol.ability.AbilityBuilder;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PassiveBuilder extends AbilityBuilder {

    protected HashMap<Byte, Set<Byte>> raceFormMap = new HashMap<>();
    protected double spiritFillModifier = 1;
    protected double spiritBonus = 1;
    protected double costModifier = 1;
    protected Set<EnumFillMethod> fillMethods = new HashSet<>();

    public PassiveBuilder addRaceForm(byte race, byte id){
        if(!raceFormMap.containsKey(race))
            raceFormMap.put(race, new HashSet<>());

        Set<Byte> formSet = raceFormMap.get(race);

        if(id >= 0)
            formSet.add(id);
        return this;
    }

    public PassiveBuilder setFillModifier(double modifier){
        this.spiritFillModifier = modifier;
        return this;
    }

    public PassiveBuilder setBonusModifier(double modifier){
        this.spiritBonus = modifier;
        return this;
    }

    public PassiveBuilder setCostModifier(double modifier){
        this.costModifier = modifier;
        return this;
    }

    public PassiveBuilder addFillMethods(EnumFillMethod... methods){
        this.fillMethods.addAll(Arrays.asList(methods));
        return this;
    }
    public PassiveBuilder addFillMethod(EnumFillMethod method){
        this.fillMethods.add(method);
        return this;
    }

    public PassiveBuilder setName(String prettyName){
        super.setName(prettyName);
        return this;
    }
    public PassiveBuilder setPrettyName(IChatComponent name){
        super.setPrettyName(name);
        return this;
    }
    public PassiveBuilder setPrettyName(String name){
        super.setPrettyName(name);
        return this;
    }

    public PassiveBuilder setId(String literalId){
        super.setId(literalId);
        return this;
    }

    public PassiveBuilder setDescription(String prettyDescription){
        super.setDescription(prettyDescription);
        return this;
    }
    public PassiveBuilder setPrettyDescription(IChatComponent description){
        super.setPrettyDescription(description);
        return this;
    }
    public PassiveBuilder setPrettyDescription(String description){
        super.setPrettyDescription(description);
        return this;
    }

    public PassiveAbility getAbility(){
        fixNonPrettyNames();
        if(fillMethods.isEmpty())
            fillMethods.add(EnumFillMethod.DAMAGE_DEALT);
        return new PassiveAbility(literalId, prettyName, prettyDescription, spiritBonus, spiritFillModifier, costModifier, raceFormMap, fillMethods);
        //return new PassiveAbility(this.name, this.raceId, this.formId, this.spiritFillModifier, this.spiritBonus, this.costModifier, this.description);
    }

}
