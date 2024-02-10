package com.mighty.spiritcontrol.ability;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.attack.Attack;
import com.mighty.spiritcontrol.ability.attack.AttackBuilder;
import com.mighty.spiritcontrol.ability.attack.EnumAttackColor;
import com.mighty.spiritcontrol.ability.attack.EnumAttackType;
import com.mighty.spiritcontrol.ability.passive.EnumFillMethod;
import com.mighty.spiritcontrol.ability.passive.PassiveAbility;
import com.mighty.spiritcontrol.ability.passive.PassiveBuilder;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/** Stores all Spirit Control Abilities **/
public class AbilityDatabase {
    private static final Map<String, Attack> attackHashMap = new CaseInsensitiveMap<>();
    private static final Map<String, Attack> ultimateHashMap = new CaseInsensitiveMap<>();
    private static final Map<String, PassiveAbility> passiveAbilityHashMap = new CaseInsensitiveMap<>();

    private static AbilityDatabase instance;

    public static AbilityDatabase getInstance(){
        if(instance == null)
            instance = new AbilityDatabase();
        return instance;
    }


    //Type: 0 = Wave, 1 = Blast, 2 = Disk, 3 = laser, 4 = spiral, 5 = large blast,  6 = barrage, 7 = shield

    //Color: 0 = purple, 1 = white, 2 = blue, 3 = purple, 4 = red, 5 = black, 6 = green, 7 = yellow, 8 = orange

    public static void loadDefaults(){
        purgeAbilities();

        SpiritControl.LOGGER.info("===REGISTERING DEFAULT ABILITIES!===");

        registerAbility( new PassiveBuilder()
                        .setId("VirtuousSpirit")
                        .setName("Virtuous Spirit")
                        .setDescription("A calm mind makes your Super Attacks cost 0.9x as much.")
                        .setCostModifier(0.9)
                        .setBonusModifier(1)
                        .addFillMethod(EnumFillMethod.DAMAGE_TAKEN, 1.0)
                        .addFillMethod(EnumFillMethod.DAMAGE_DEALT, 1.5)
                        .getAbility()
        );

        registerAbility( new AttackBuilder()
                        .setId("KiAttack")
                        .setName("Ki Attack")
                        .setDescription("Let loose the Spirit you've stored in a small blast")
                        .setType(EnumAttackType.BARRAGE)
                        .setColor(EnumAttackColor.GREEN)
                        .setDmgModifier(1)
                        .setCost(0.1)
                        .setCooldown(1)
                        .setCasttime(0.5)
                        .getAbility()
        );

        registerAbility( new AttackBuilder()
                        .setId("EnergyWave")
                        .setName("Full Power Energy Wave")
                        .setDescription("Burn out your entire Spirit Gauge in a mighty wave")
                        .setUltimate(true)
                        .setType(EnumAttackType.WAVE)
                        .setColor(EnumAttackColor.ORANGE)
                        .setCost(1)
                        .setCooldown(5)
                        .setCasttime(1)
                        .setDmgModifier(1)
                        .setFatigue(5)
                        .getAbility()
        );
    }

    /**
     * Register a new ability to the database, or overwrite existing ones
     * @param ability ability to register
     */
    public static void registerAbility(Ability ability){
        if(ensureProperType(ability)) {
            if (ability instanceof Attack)
                registerAttack((Attack) ability);
            if (ability instanceof PassiveAbility)
                registerPassive((PassiveAbility) ability);
        }
    }

    //Checks if the new ability doesn't somehow switch around default ability types
    private static boolean ensureProperType(Ability ability){
        if(ability == null)
            return false;
        if(isRegistered(ability.getId()))
            return ability.getClass() == getAbilityById(ability.getId()).getClass();
        return true;
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

    /**
     * @param ability ability to check
     * @return If the ability is a default ability
     */
    public static boolean isDefault(Ability ability){
        if(ability == getAbilityById("VirtuousSpirit"))
            return true;
        if(ability == getAbilityById("KiAttack"))
            return true;
        return ability == getAbilityById("EnergyWave");
    }

    private static boolean isAttack(String attId){
        return attackHashMap.containsKey(attId);
    }
    private static boolean isUltimate(String ultId){
        return ultimateHashMap.containsKey(ultId);
    }
    private static boolean isPassive(String passiveId){
        return passiveAbilityHashMap.containsKey(passiveId);
    }

    /**
     * @param abilityId ability ID string
     * @return If that ability is loaded
     */
    public static boolean isRegistered(String abilityId){
        return isPassive(abilityId) || isAttack(abilityId) || isUltimate(abilityId);
    }

    /**
     * Checks if an ability is loaded
     * @param ability ability to check
     * @return if that ability is loaded
     */
    public static boolean isRegistered(Ability ability) {
        return isRegistered(ability.getId());
    }

    /**
     * @return Array of all registered IDs
     */
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
     *
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

    /**
     * Purges all abilities from the database
     */
    private static void purgeAbilities(){
        SpiritControl.LOGGER.info("===PURGING ALL LOADED ABILITIES===");
        attackHashMap.clear();
        ultimateHashMap.clear();
        passiveAbilityHashMap.clear();
    }

    /**
     * @return Default super attack
     */
    public static Ability getDefaultSuper(){
        return getAbilityById("KiAttack");
    }

    /**
     * @return Default ultimate attack
     */
    public static Ability getDefaultUltimate(){
        return getAbilityById("EnergyWave");
    }

    /**
     * @return Default passive ability
     */
    public static Ability getDefaultPassive() {
        return getAbilityById("VirtuousSpirit");
    }
}
