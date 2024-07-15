package com.mighty.spiritcontrol.config.reader.objects;

import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.attack.AttackBuilder;
import com.mighty.spiritcontrol.ability.attack.EnumAttackColor;
import com.mighty.spiritcontrol.ability.attack.EnumAttackType;


/**
 * Intermediate class used for loading attacks.
 *
 * Allows to make more complex types more readable in the configs.
 */
public class AttackJSON {
    public String name;
    public String description;
    public String color;
    public String type;
    public int speed;
    public int density;
    public boolean effect;
    public double dmg_modifier;
    public double cost_modifier;
    public double cast_time;

    public double cooldown;
    public String fire_message;
    public double fatigue;

    public Ability getAbility(String id, boolean isUltimate) {
        return new AttackBuilder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setUltimate(isUltimate)
                .setColor(color)
                .setType(type)
                .setSpeed(speed)
                .setDensity(density)
                .setEffect(effect)
                .setDmgModifier(dmg_modifier)
                .setCost(cost_modifier)
                .setFireMessage(fire_message)
                .setCasttime(cast_time)
                .setCooldown(cooldown)
                .setFatigue(fatigue)
                .getAbility();
    }

    public void setExampleValues(){
        name = "Name. Can be formatted (Only MC Codes &awork.)";
        description = "Description. Can be formatted (Only MC Codes &awork.)";

        color = EnumAttackColor.ALIGNMENT_BASED.name().toLowerCase();
        type = EnumAttackType.BIG_BLAST.name().toLowerCase();
        speed = 5;
        density = 12;
        effect = false;
        dmg_modifier = 1.0;
        cost_modifier = 1.0;
        fire_message = "Example fire message. SHOULD be formatted. Uses both <aqua>MiniMessage</aqua> and &cMC &cCodes";
        cast_time = 1.0;
        fatigue = 0.0;
    }
}
