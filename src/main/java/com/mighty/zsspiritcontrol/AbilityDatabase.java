package com.mighty.zsspiritcontrol;
import com.mighty.zsspiritcontrol.Attack;
import com.mighty.zsspiritcontrol.PassiveAbility;

/** Stores all Spirit Control Abilities **/
public class AbilityDatabase {

    /** Returns all attack names. Used in commands such as 'unlock.'
     *
     * @return String[] of all attack names.
     */
    public static String[] getAllAttackNames() {
        // Bless me Lord for I have sinned
        String[] attackNames = {
                "KiAttack", "EnergyWave", "GalickGun", "FinalFlash", "BigBangAttack",
                "SuperSpiritBomb", "MouthBlast", "SpecialBeamCannon", "CandyBeam",
                "Kamehameha", "BurningAttack", "GTKamehameha", "FinalShine",
                "Hakai", "GammaBurstFlash", "BigBangKamehameha", "FinalKamehameha"
        };

        return attackNames;
    }

    /** Returns all passive names. Used in commands such as 'unlock.'
     *
     * @return String[] of all passive names.
     */
    public static String[] getAllPassiveNames() {
        // Bless me Lord for I have sinned
        String[] passiveNames = {
                "VirtuousSpirit", "ForcedSpiritFission", "PowerOfEgo", "SaiyanBeyondGod",
                "TrueUltraInstinct", "OverflowingEnergy", "CalmMind", "SuperRegeneration",
                "MaxPower", "FlameOfHope", "HonedMind", "UltimateEvolution"
        };

        return passiveNames;
    }



    // DEFAULT SUPER ATTACK
    public static Attack getKiAttack(){
    return new Attack("KiAttack",6,7,5,0,1.0,1.0,1.0,"Hyaa!","Ki Attack - Let loose the Spirit you've stored in a small blast.",false,0.0);
    }

    // DEFAULT ULTIMATE ATTACK
    public static Attack getEnergyWave(){
        return new Attack("EnergyWave",0,8,5,0,1.0,1.0,1.0,"TAKE THIS!","Full Power Energy Wave - Burn out your entire Spirit Gauge in a mighty wave.",true,1.0);
    }

    // DEFAULT PASSIVE
    public static PassiveAbility getVirtuousSpirit(){
        return new PassiveAbility("VirtuousSpirit",-1,-1,1.0,1.0,0.9,"Virtuous Spirit - A calm mind makes your Super Attacks cost 0.9x as much.");
    }

    // Super Attack
    public static Attack getGalickGun(){
        return new Attack("GalickGun",0,3,5,0,1.0,1.0,1.0,"GALICK GUN!","Galick Gun - Unleash Saiyan Pride with Vegeta's signature wave.",false,0.0);
    }

    // Ultimate Attack
    public static Attack getFinalFlash(){
        return new Attack("FinalFlash",0,7,5,0,1.0,1.0,1.0,"FINAL FLASH!","Final Flash - Let loose your entire gauge with Vegeta's finishing move.",true,1.0);
    }

    // Super Attack
    public static Attack getBigBangAttack(){
        return new Attack("BigBangAttack",1,1,5,1,1.0,1.0,1.0,"BIG BANG ATTACK!","Big Bang Attack - Launch a ball of exploding Ki.",false,0.0);
    }

    // Ultimate Attack
    public static Attack getSuperSpiritBomb(){
        return new Attack("SuperSpiritBomb",5,1,1,1,1.0,1.0,1.0,"I hope you come back someday... As a better person! See you later!","Super Spirit Bomb - Channel energy from across the Server into the ultimate weapon.",true,1.0);
    }

    // Super Attack
    public static Attack getMouthBlast(){
        return new Attack("MouthBlast",1,6,5,1,1.0,1.0,1.0,"HAA!","Mouth Blast - Shoots a Blast from (you guessed it) your mouth!",false,0.0);
    }

    // Ultimate Attack
    public static Attack getSpecialBeamCannon(){
        return new Attack("SpecialBeamCannon",4,8,5,0,1.0,1.0,1.0,"SPECIAL BEAM CANNON!","Special Beam Cannon - Pierce enemies with Piccolo's signature attack.",true,1.0);
    }

    // Ultimate Attack
    public static Attack getCandyBeam(){
        return new Attack("CandyBeam",3,3,5,0,1.0,1.0,1.0,"TURN INTO CHOCOLATE!","Candy Beam - Buu turn you into chocolate and eat you.",true,1.0);
    }

    // Super Attack
    public static Attack getKamehameha(){
        return new Attack("Kamehameha",0,2,5,0,1.0,1.0,1.0,"KAME-HAME-HAAAA!","Kamehameha - Unleash the Turtle School's iconic Ki Wave.",false,0.0);
    }

    // Super Attack
    public static Attack getBurningAttack(){
        return new Attack("BurningAttack",1,8,5,1,1.0,1.0,1.0,"Burning Attack!","Burning Attack - Protect the future with Trunks' signature blast.",false,0.0);
    }

    // Passive
    public static PassiveAbility getForcedSpiritFission(){
        return new PassiveAbility("ForcedSpiritFission",-1,-1,1.0,1.0,1.0,"Forced Spirit Fission - Drawing Spirit from your opponents fills your Spirit Gauge x as fast.");
    }

    // Passive
    public static PassiveAbility getPowerOfEgo(){
        return new PassiveAbility("PowerOfEgo",-1,-1,1.0,1.0,1.0,"Power of Ego - Your Spirit Gauge now fills upon taking damage.");
    }

    // Passive
    public static PassiveAbility getSaiyanBeyondGod(){
        return new PassiveAbility("SaiyanBeyondGod",-1,-1,1.0,1.0,1.0,"Saiyan Beyond God - As a Full/Half Saiyan, Mystic fills your gauge x as fast, while Blue/Rose fill it x as fast.");
    }

    // Passive
    public static PassiveAbility getTrueUltraInstinct(){
        return new PassiveAbility("TrueUltraInstinct",-1,-1,1.0,1.0,1.0,"True Ultra Instinct - As a Full/Half Saiyan, UI fills your gauge x as fast, while SSJ4 fills it x as fast.");
    }

    // Passive
    public static PassiveAbility getOverflowingEnergy(){
        return new PassiveAbility("OverflowingEnergy",-1,-1,1.0,1.0,1.0,"Overflowing Energy - While using SSJ1, gain Spirit passively over time.");
    }

    // Passive
    public static PassiveAbility getCalmMind(){
        return new PassiveAbility("CalmMind",-1,-1,1.0,1.0,1.0,"Calm Mind - Gain Spirit while charging Ki.");
    }

    // Passive
    public static PassiveAbility getSuperRegeneration(){
        return new PassiveAbility("SuperRegeneration",-1,-1,1.0,1.0,1.0,"Super Regeneration - Greatly replenish your Spirit while regenerating as a Majin.");
    }

    // Passive
    public static PassiveAbility getMaxPower(){
        return new PassiveAbility("MaxPower",-1,-1,1.0,1.0,1.0,"Max Power - As a Human, using buffed form fills your Spirit Gauge x as fast.");
    }

    // Passive
    public static PassiveAbility getFlameOfHope(){
        return new PassiveAbility("FlameOfHope",-1,-1,1.0,1.0,1.0,"Flame of Hope - Your Spirit Gauge fills x as fast while using SSG.");
    }

    // Passive
    public static PassiveAbility getHonedMind(){
        return new PassiveAbility("HonedMind",-1,-1,1.0,1.0,1.0,"Honed Mind - Your Spirit Gauge Capacity increases by x.");
    }

    // Passive
    public static PassiveAbility getUltimateEvolution(){
        return new PassiveAbility("UltimateEvolution",-1,-1,1.0,1.0,1.0,"Ultimate Evolution - Your Spirit Gauge fills x as fast while using Arco Ultimate or Godform.");
    }

    // Ultimate Attack
    public static Attack getGTKamehameha(){
        return new Attack("GTKamehameha",0,4,5,0,1.0,1.0,1.0,"KAMEHAMEHA! TIME TEEEEN!","Kamehameha x10 - Take your enemy on a grand tour with this limited-edition Ultimate.",true,1.0);
    }

    // Ultimate Attack
    public static Attack getFinalShine(){
        return new Attack("FinalShine",0,6,5,0,1.0,1.0,1.0,"FINAL SHINE ATTACK!","Final Shine - Take your enemy on a grand tour with this limited-edition Ultimate.",true,1.0);
    }

    // Ultimate Attack
    public static Attack getHakai(){
        return new Attack("Hakai",1,4,5,1,1.0,1.0,1.0,"HAKAI!","Hakai - Destroy your opponent with this limited-edition Ultimate.",true,1.0);
    }

    // Ultimate Attack
    public static Attack getGammaBurstFlash(){
        return new Attack("GammaBurstFlash",0,3,5,1,1.0,1.0,1.0,"GAMMA BURST FLASH!","Gamma Burst Flash - Save the Future with this limited-edition Ultimate.",true,1.0);
    }

    // Ultimate Attack
    public static Attack getBigBangKamehameha(){
        return new Attack("BigBangKamehameha",0,2,5,1,1.0,1.0,1.0,"BIG BANG... KAMEHAMEHA!","Big Bang Kamehameha - Take your place as the Ultimate Warrior with this limited-edition Ultimate.",true,1.0);
    }

    // Ultimate Attack
    public static Attack getFinalKamehameha(){
        return new Attack("FinalKamehameha",0,2,5,1,1.0,1.0,1.0,"Final... KAMEHAMEHA!","Final Kamehameha - Take your place as the Ultimate Warrior with this limited-edition Ultimate.",true,1.0);
    }

    //Type: 0 = Wave, 1 = Blast, 2 = Disk, 3 = laser, 4 = spiral, 5 = large blast,  6 = barrage, 7 = shield

    //Color: 0 = purple, 1 = white, 2 = blue, 3 = purple, 4 = red, 5 = black, 6 = green, 7 = yellow, 8 = orange

    /**
     * Given an Attack name, returns the attack in question.
     * @param attackName - Name query
     * @return Attack or null.
     */
    public static Attack getAttackByName(String attackName) {
        // I'm a bad programmer
        switch (attackName) {
            case "KiAttack":
                return getKiAttack();
            case "EnergyWave":
                return getEnergyWave();
            case "GalickGun":
                return getGalickGun();
            case "FinalFlash":
                return getFinalFlash();
            case "BigBangAttack":
                return getBigBangAttack();
            case "SuperSpiritBomb":
                return getSuperSpiritBomb();
            case "MouthBlast":
                return getMouthBlast();
            case "SpecialBeamCannon":
                return getSpecialBeamCannon();
            case "CandyBeam":
                return getCandyBeam();
            case "Kamehameha":
                return getKamehameha();
            case "BurningAttack":
                return getBurningAttack();
            case "GTKamehameha":
                return getGTKamehameha();
            case "FinalShine":
                return getFinalShine();
            case "Hakai":
                return getHakai();
            case "GammaBurstFlash":
                return getGammaBurstFlash();
            case "BigBangKamehameha":
                return getBigBangKamehameha();
            case "FinalKamehameha":
                return getFinalKamehameha();
            default:
                return null;
        }
    }

    // Function to get a PassiveAbility by name
    public static PassiveAbility getPassiveByName(String passiveName) {
        switch (passiveName) {
            case "VirtuousSpirit":
                return getVirtuousSpirit();
            case "ForcedSpiritFission":
                return getForcedSpiritFission();
            case "PowerOfEgo":
                return getPowerOfEgo();
            case "SaiyanBeyondGod":
                return getSaiyanBeyondGod();
            case "TrueUltraInstinct":
                return getTrueUltraInstinct();
            case "OverflowingEnergy":
                return getOverflowingEnergy();
            case "CalmMind":
                return getCalmMind();
            case "SuperRegeneration":
                return getSuperRegeneration();
            case "MaxPower":
                return getMaxPower();
            case "FlameOfHope":
                return getFlameOfHope();
            case "HonedMind":
                return getHonedMind();
            case "UltimateEvolution":
                return getUltimateEvolution();
            default:
                return null;
        }
    }
}
