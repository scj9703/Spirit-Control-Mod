package com.mighty.zsspiritcontrol.ability.passive;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.ability.AbilityBuilder;
import kamkeel.zslib.util.dbc.enums.RaceEnum;

import java.util.*;

public class PassiveBuilder extends AbilityBuilder {

    protected HashMap<Byte, Set<Byte>> raceFormMap = new HashMap<>();
    protected double spiritFillModifier = 1;
    protected double spiritBonus = 1;
    protected double costModifier = 1;
    protected Set<EnumFillMethod> fillMethods = new HashSet<>();

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
        if(methods != null)
            this.fillMethods.addAll(Arrays.asList(methods));
        return this;
    }
    public PassiveBuilder addFillMethod(EnumFillMethod method){
        if(method != null)
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
                SpiritControl.INSTANCE.LOGGER.warn("Can't find race '"+raceName+"'. ", e);
            }

        }
        return this;
    }

    public PassiveBuilder addFillMethods(Set<String> fillMethodsNew) {
        for(String methodName : fillMethodsNew){
            try{
                this.addFillMethod(EnumFillMethod.valueOf(methodName.toUpperCase()));
            }catch(Exception e){
                SpiritControl.INSTANCE.LOGGER.warn("Can't find fill method '"+methodName+"'. ", e);
            }
        }
        return this;
    }

    public PassiveAbility getAbility(){
        if(fillMethods.isEmpty())
            fillMethods.add(EnumFillMethod.DAMAGE_DEALT);
        return new PassiveAbility(literalId, name, description, spiritBonus, spiritFillModifier, costModifier, raceFormMap, fillMethods);
        //return new PassiveAbility(this.name, this.raceId, this.formId, this.spiritFillModifier, this.spiritBonus, this.costModifier, this.description);
    }
}
