package com.mighty.zsspiritcontrol.attack;

import com.mighty.zsspiritcontrol.SpiritControl;
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
        //Register attacks
        registerAttack(new Attack("KiAttack",6,7,5,0,1.0,1.0,1.0,"Hyaa!","Ki Attack - Let loose the Spirit you've stored in a small blast.",false,0.0));
        registerAttack(new Attack("GalickGun",0,3,5,0,1.0,1.0,1.0,"GALICK GUN!","Galick Gun - Unleash Saiyan Pride with Vegeta's signature wave.",false,0.0));
        registerAttack(new Attack("BigBangAttack",1,1,5,1,1.0,1.0,1.0,"BIG BANG ATTACK!","Big Bang Attack - Launch a ball of exploding Ki.",false,0.0));
        registerAttack(new Attack("SuperSpiritBomb",5,1,1,1,1.0,1.0,1.0,"I hope you come back someday... As a better person! See you later!","Super Spirit Bomb - Channel energy from across the Server into the ultimate weapon.",true,1.0));
        registerAttack(new Attack("MouthBlast",1,6,5,1,1.0,1.0,1.0,"HAA!","Mouth Blast - Shoots a Blast from (you guessed it) your mouth!",false,0.0));
        registerAttack(new Attack("SpecialBeamCannon",4,8,5,0,1.0,1.0,1.0,"SPECIAL BEAM CANNON!","Special Beam Cannon - Pierce enemies with Piccolo's signature attack.",true,1.0));
        registerAttack(new Attack("CandyBeam",3,3,5,0,1.0,1.0,1.0,"TURN INTO CHOCOLATE!","Candy Beam - Buu turn you into chocolate and eat you.",true,1.0));
        registerAttack(new Attack("Kamehameha",0,2,5,0,1.0,1.0,1.0,"KAME-HAME-HAAAA!","Kamehameha - Unleash the Turtle School's iconic Ki Wave.",false,0.0));
        registerAttack(new Attack("BurningAttack",1,8,5,1,1.0,1.0,1.0,"Burning Attack!","Burning Attack - Protect the future with Trunks' signature blast.",false,0.0));



        registerAttack(new Attack("EnergyWave",0,8,5,0,1.0,1.0,1.0,"TAKE THIS!","Full Power Energy Wave - Burn out your entire Spirit Gauge in a mighty wave.",true,1.0));
        registerAttack(new Attack("FinalFlash",0,7,5,0,1.0,1.0,1.0,"FINAL FLASH!","Final Flash - Let loose your entire gauge with Vegeta's finishing move.",true,1.0));
        registerAttack(new Attack("SpecialBeamCannon",4,8,5,0,1.0,1.0,1.0,"SPECIAL BEAM CANNON!","Special Beam Cannon - Pierce enemies with Piccolo's signature attack.",true,1.0));
        registerAttack(new Attack("GTKamehameha",0,4,5,0,1.0,1.0,1.0,"KAMEHAMEHA! TIME TEEEEN!","Kamehameha x10 - Take your enemy on a grand tour with this limited-edition Ultimate.",true,1.0));
        registerAttack(new Attack("FinalShine",0,6,5,0,1.0,1.0,1.0,"FINAL SHINE ATTACK!","Final Shine - Take your enemy on a grand tour with this limited-edition Ultimate.",true,1.0));
        registerAttack(new Attack("Hakai",1,4,5,1,1.0,1.0,1.0,"HAKAI!","Hakai - Destroy your opponent with this limited-edition Ultimate.",true,1.0));
        registerAttack(new Attack("GammaBurstFlash",0,3,5,1,1.0,1.0,1.0,"GAMMA BURST FLASH!","Gamma Burst Flash - Save the Future with this limited-edition Ultimate.",true,1.0));
        registerAttack(new Attack("BigBangKamehameha",0,2,5,1,1.0,1.0,1.0,"BIG BANG... KAMEHAMEHA!","Big Bang Kamehameha - Take your place as the Ultimate Warrior with this limited-edition Ultimate.",true,1.0));
        registerAttack(new Attack("FinalKamehameha",0,2,5,1,1.0,1.0,1.0,"Final... KAMEHAMEHA!","Final Kamehameha - Take your place as the Ultimate Warrior with this limited-edition Ultimate.",true,1.0));

        //Register passives
        registerPassive(new PassiveAbility("ForcedSpiritFission",-1,-1,1.0,1.0,1.0,"Forced Spirit Fission - Drawing Spirit from your opponents fills your Spirit Gauge x as fast."));
        registerPassive(new PassiveAbility("VirtuousSpirit",-1,-1,1.0,1.0,0.9,"Virtuous Spirit - A calm mind makes your Super Attacks cost 0.9x as much."));
        registerPassive(new PassiveAbility("PowerOfEgo",-1,-1,1.0,1.0,1.0,"Power of Ego - Your Spirit Gauge now fills upon taking damage."));
        registerPassive(new PassiveAbility("SaiyanBeyondGod",-1,-1,1.0,1.0,1.0,"Saiyan Beyond God - As a Full/Half Saiyan, Mystic fills your gauge x as fast, while Blue/Rose fill it x as fast."));
        registerPassive(new PassiveAbility("TrueUltraInstinct",-1,-1,1.0,1.0,1.0,"True Ultra Instinct - As a Full/Half Saiyan, UI fills your gauge x as fast, while SSJ4 fills it x as fast."));
        registerPassive(new PassiveAbility("OverflowingEnergy",-1,-1,1.0,1.0,1.0,"Overflowing Energy - While using SSJ1, gain Spirit passively over time."));
        registerPassive(new PassiveAbility("CalmMind",-1,-1,1.0,1.0,1.0,"Calm Mind - Gain Spirit while charging Ki."));
        registerPassive(new PassiveAbility("SuperRegeneration",-1,-1,1.0,1.0,1.0,"Super Regeneration - Greatly replenish your Spirit while regenerating as a Majin."));
        registerPassive(new PassiveAbility("MaxPower",-1,-1,1.0,1.0,1.0,"Max Power - As a Human, using buffed form fills your Spirit Gauge x as fast."));
        registerPassive(new PassiveAbility("FlameOfHope",-1,-1,1.0,1.0,1.0,"Flame of Hope - Your Spirit Gauge fills x as fast while using SSG."));
        registerPassive(new PassiveAbility("HonedMind",-1,-1,1.0,1.0,1.0,"Honed Mind - Your Spirit Gauge Capacity increases by x."));
        registerPassive(new PassiveAbility("UltimateEvolution",-1,-1,1.0,1.0,1.0,"Ultimate Evolution - Your Spirit Gauge fills x as fast while using Arco Ultimate or Godform."));
    }

    public static void registerAttack(Attack attack){
        if(attack.isUltimate())
            ultimateHashMap.put(attack.getName(), attack);
        else
            attackHashMap.put(attack.getName(), attack);

        SpiritControl.LOGGER.info("Adding" + (attack.isUltimate() ? " Ultimate " : " ") + "Attack: "+attack.getName());
    }

    public static void registerPassive(PassiveAbility passive){
        passiveAbilityHashMap.put(passive.getName(), passive);
        SpiritControl.LOGGER.info("Adding Passive Ability: "+passive.getName());
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
        return isRegistered(ability.getName());
    }


    public static String[] getRegisteredNames(){
        return (String[]) ArrayUtils.addAll(getAllAttackNames(), getAllUltimateNames(), getAllPassiveNames());
    }


    /**
     * Used in commands such as 'unlock.'
     * @return String array of all ultimate names.
     */
    public static String[] getAllUltimateNames(){
        return ultimateHashMap.keySet().toArray(new String[0]);
    }

    /**
     * Used in commands such as 'unlock.'
     * @return String array of all attack names.
     */
    public static String[] getAllAttackNames() {
        return attackHashMap.keySet().toArray(new String[0]);
    }

    /**
     * Used in commands such as 'unlock.'
     * @return String array of all passive names.
     */
    public static String[] getAllPassiveNames() {
        return passiveAbilityHashMap.keySet().toArray(new String[0]);
    }

    /**
     * Used in finding attacks for example when loading playerdata.
     * @param name name of the ability
     * @return An instace of `Ability` if one was registered, otherwise null
     */
    public static Ability getAbilityByName(String name){
        if(isPassive(name))
            return passiveAbilityHashMap.get(name);
        if(isAttack(name))
            return attackHashMap.get(name);
        if(isUltimate(name))
            return ultimateHashMap.get(name);

        return null;
    }

}
