package com.mighty.spiritcontrol.config.reader.objects;

import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.passive.PassiveBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Intermediate class used for loading attacks.
 *
 * Allows to make more complex types more readable in the configs.
 */
public class PassiveJSON {

    public String name;
    public String description;

    double spirit_bonus_modifier;

    double spirit_cost_modifier;
    Map<String, Set<Byte>> race_forms;
    Map<String, Double> fill_method_modifiers;

    public Ability getAbility(String id) {
        return new PassiveBuilder().setId(id)
                .setName(name)
                .setDescription(description)
                .addRaceFormMap(race_forms)
                .addFillMethodsByNameMap(fill_method_modifiers)
                .setBonusModifier(spirit_bonus_modifier)
                .setCostModifier(spirit_cost_modifier)
                .getAbility();
    }

    public void setExampleValues(){
        name = "Name. Can be formatted (Only MC Codes &awork.)";
        description = "Description. Can be formatted (Only MC Codes &awork.)";

        spirit_bonus_modifier = 1.0;
        spirit_cost_modifier = 1.0;

        race_forms = new HashMap<>();
        fill_method_modifiers = new HashMap<>();

        Set<Byte> formsForRace = new HashSet<>();
        formsForRace.add((byte) -1);
        race_forms.put("half_saiyan", formsForRace);
        race_forms.put("saiyan", formsForRace);
        fill_method_modifiers.put("damage_dealt", 1.0);
    }

}
