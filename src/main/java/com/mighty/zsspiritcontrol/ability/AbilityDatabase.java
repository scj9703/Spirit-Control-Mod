package com.mighty.zsspiritcontrol.ability;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.ability.attack.Attack;
import com.mighty.zsspiritcontrol.ability.attack.AttackBuilder;
import com.mighty.zsspiritcontrol.ability.passive.PassiveAbility;
import com.mighty.zsspiritcontrol.ability.passive.PassiveBuilder;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackColor;
import kamkeel.zslib.util.dbc.enums.kiattack.EnumAttackType;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;

/** Stores all Spirit Control Abilities **/
public class AbilityDatabase {
    private static final HashMap<String, Attack> attackHashMap = new HashMap<>();
    private static final HashMap<String, Attack> ultimateHashMap = new HashMap<>();
    private static final HashMap<String, PassiveAbility> passiveAbilityHashMap = new HashMap<>();


    //Type: 0 = Wave, 1 = Blast, 2 = Disk, 3 = laser, 4 = spiral, 5 = large blast,  6 = barrage, 7 = shield

    //Color: 0 = purple, 1 = white, 2 = blue, 3 = purple, 4 = red, 5 = black, 6 = green, 7 = yellow, 8 = orange
    static{
        registerAbility(
                new PassiveBuilder()
                        .setName("Virtuous Spirit")
                        .setDescription("A calm mind makes your Super Attacks cost 0.9x as much.")
                        .setFillModifier(1)
                        .setCostModifier(0.9)
                        .setBonusModifier(1)
                        .getAbility()
        );

        registerAbility(
                new AttackBuilder()
                        .setName("Ki Attack")
                        .setDescription("Let loose the Spirit you've stored in a small blast")
                        .setType(EnumAttackType.BARRAGE)
                        .setColor(EnumAttackColor.GREEN)
                        .setDmgModifier(1)
                        .setCost(1)
                        .setCasttime(1)
                        .getAbility()
        );

        registerAbility(
                new AttackBuilder()
                        .setId("EnergyWave")
                        .setName("Full Power Energy Wave")
                        .setDescription("Burn out your entire Spirit Gauge in a mighty wave")
                        .setUltimate(true)
                        .setType(EnumAttackType.WAVE)
                        .setColor(EnumAttackColor.ORANGE)
                        .setCost(1)
                        .setCasttime(1)
                        .setDmgModifier(1)
                        .setFatigue(1)
                        .getAbility()
        );
    }

    public static void registerAbility(Ability ability){
        if(ability instanceof Attack)
            registerAttack((Attack) ability);
        if(ability instanceof PassiveAbility)
            registerPassive((PassiveAbility) ability);
    }

    private static void registerAttack(Attack attack){
        if(attack.isUltimate())
            ultimateHashMap.put(attack.getId(), attack);
        else
            attackHashMap.put(attack.getId(), attack);

        SpiritControl.LOGGER.info("Adding" + (attack.isUltimate() ? " Ultimate " : " ") + "Attack: "+attack.getId());
    }

    private static void registerPassive(PassiveAbility passive){
        passiveAbilityHashMap.put(passive.getId(), passive);
        SpiritControl.LOGGER.info("Adding Passive Ability: "+passive.getId());
    }

    public static boolean isDefault(Ability ability){
        if(ability == getAbilityById("VirtuousSpirit"))
            return true;
        if(ability == getAbilityById("KiAttack"))
            return true;
        return ability == getAbilityById("EnergyWave");
    }

    private static boolean isAttack(String attName){
        return attackHashMap.containsKey(attName);
    }
    private static boolean isUltimate(String ultName){
        return ultimateHashMap.containsKey(ultName);
    }
    private static boolean isPassive(String passiveName){
        return passiveAbilityHashMap.containsKey(passiveName);
    }
    public static boolean isRegistered(String abilityName){
        return isPassive(abilityName) || isAttack(abilityName) || isUltimate(abilityName);
    }
    public static boolean isRegistered(Ability ability) {
        return isRegistered(ability.getId());
    }


    public static String[] getRegisteredIds(){
        return ArrayUtils.addAll(ArrayUtils.addAll(getAllAttackIds(), getAllUltimateIds()), getAllPassiveIds());
    }


    /**
     * Used in commands such as 'unlock.'
     * @return String array of all ultimate names.
     */
    public static String[] getAllUltimateIds(){
        return ultimateHashMap.keySet().toArray(new String[0]);
    }

    /**
     * Used in commands such as 'unlock.'
     * @return String array of all attack names.
     */
    public static String[] getAllAttackIds() {
        return attackHashMap.keySet().toArray(new String[0]);
    }

    /**
     * Used in commands such as 'unlock.'
     * @return String array of all passive names.
     */
    public static String[] getAllPassiveIds() {
        return passiveAbilityHashMap.keySet().toArray(new String[0]);
    }

    /**
     * Used in finding attacks for example when loading playerdata.
     * @param name name of the ability
     * @return An instace of `Ability` if one was registered, otherwise null
     */
    public static Ability getAbilityById(String name){
        if(isPassive(name))
            return passiveAbilityHashMap.get(name);
        if(isAttack(name))
            return attackHashMap.get(name);
        if(isUltimate(name))
            return ultimateHashMap.get(name);

        return null;
    }

}
