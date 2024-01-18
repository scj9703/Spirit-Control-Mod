package com.mighty.zsspiritcontrol.ability.passive;

import com.mighty.zsspiritcontrol.ability.AbilityBuilder;
import kamkeel.zslib.util.dbc.enums.RaceEnum;

import java.util.HashSet;
import java.util.Set;

public class PassiveBuilder extends AbilityBuilder {

    protected Set<RaceEnum> raceId = new HashSet<>();
    protected Set<Byte> formId = new HashSet<>();
    protected double spiritFillModifier = 1;
    protected double spiritBonus = 1;
    protected double costModifier = 1;
    protected Set<EnumFillMethod> fillMethods = new HashSet<>();

    public PassiveBuilder addRace(RaceEnum race){
        this.raceId.add(race);
        return this;
    }

    public PassiveBuilder addForm(int id){
        this.formId.add((byte) id);
        return this;
    }

    public PassiveBuilder setFillModifier(int modifier){
        this.spiritFillModifier = modifier;
        return this;
    }

    public PassiveBuilder setBonusModifier(int modifier){
        this.spiritBonus = modifier;
        return this;
    }

    public PassiveBuilder setCostModifier(int modifier){
        this.costModifier = modifier;
        return this;
    }

    public PassiveBuilder addFillMethods(Set<EnumFillMethod> methods){
        this.fillMethods.addAll(methods);
        return this;
    }
    public PassiveBuilder addFillMethod(EnumFillMethod method){
        this.fillMethods.add(method);
        return this;
    }

    public PassiveBuilder setName(String name){
        super.setName(name);
        return this;
    }

    public PassiveBuilder setId(String literalId){
        super.setId(literalId);
        return this;
    }

    public PassiveBuilder setDescription(String description){
        super.setDescription(description);
        return this;
    }

    public PassiveAbility getAbility(){
        return null;
        //return new PassiveAbility(this.name, this.raceId, this.formId, this.spiritFillModifier, this.spiritBonus, this.costModifier, this.description);
    }

}
