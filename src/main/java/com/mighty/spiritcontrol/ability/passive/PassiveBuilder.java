package com.mighty.spiritcontrol.ability.passive;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.AbilityBuilder;
import kamkeel.zslib.util.dbc.enums.RaceEnum;

import java.util.*;

public class PassiveBuilder extends AbilityBuilder {

    protected HashMap<Byte, Set<Byte>> raceFormMap = new HashMap<>();
    protected HashMap<EnumFillMethod, Double> fillMethods = new HashMap<>();
    protected double spiritBonus = 1;
    protected double costModifier = 1;

    public PassiveBuilder addRaceForm(int race, int id){
        byte raceByte = (byte) race;
        byte idByte = (byte) id;

        if(!raceFormMap.containsKey(raceByte))
            raceFormMap.put(raceByte, new HashSet<>());


        if(idByte >= 0) {
            raceFormMap.get(raceByte).add(idByte);
        }
        return this;
    }

    public PassiveBuilder addFillMethod(EnumFillMethod method, double value){
        fillMethods.put(method, value);
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


    public PassiveBuilder addRaceFormMap(Map<String, Set<Byte>> racesToAdd) {
        if(racesToAdd == null)
            return this;

        for(String raceName : racesToAdd.keySet()){

            try{
                byte raceId = (byte) RaceEnum.valueOf(raceName.toUpperCase()).id;
//                raceFormMap.computeIfAbsent(key, k -> new HashSet<>());
//                raceFormMap.get(key).addAll(racesToAdd.get(raceName));
                for(byte formId : racesToAdd.get(raceName)){
                    this.addRaceForm(raceId, formId);
                }
            }catch (Exception e){
                SpiritControl.LOGGER.warn("Can't find race '"+raceName+"'. ", e);
            }

        }
        return this;
    }

    public PassiveAbility getAbility(){
        if(fillMethods.isEmpty())
            fillMethods.put(EnumFillMethod.DAMAGE_DEALT, 1.0);
        return new PassiveAbility(literalId, name, description, spiritBonus, fillMethods, costModifier, raceFormMap);
        //return new PassiveAbility(this.name, this.raceId, this.formId, this.spiritFillModifier, this.spiritBonus, this.costModifier, this.description);
    }

    public PassiveBuilder addFillMethodsByNameMap(Map<String, Double> fillMethods) {
        for(String key : fillMethods.keySet()){
            try {
                this.fillMethods.put(EnumFillMethod.valueOf(key.toUpperCase()), fillMethods.get(key));
            } catch (Exception ignored) {}
        }
        return this;
    }

    public PassiveBuilder addFillMethodsByEnumMap(Map<EnumFillMethod, Double> fillMethods) {
        for(EnumFillMethod key : fillMethods.keySet()){
            this.fillMethods.put(key, fillMethods.get(key));
        }
        return this;
    }
}
