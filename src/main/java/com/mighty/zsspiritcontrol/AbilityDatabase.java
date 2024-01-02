package com.mighty.zsspiritcontrol;
import com.mighty.zsspiritcontrol.Attack;
import com.mighty.zsspiritcontrol.PassiveAbility;

/** Stores all Spirit Control Abilities **/
public class AbilityDatabase {
    // DEFAULT SUPER ATTACK
    public Attack getKiAttack(){
    return new Attack("KiAttack",6,7,5,0,1.0,1.0,1.0,"Hyaa!","Ki Attack - Let loose the Spirit you've stored in a small blast.",false,0.0);
    }

    // DEFAULT ULTIMATE ATTACK
    public Attack getEnergyWave(){
        return new Attack("EnergyWave",0,8,5,0,1.0,1.0,1.0,"TAKE THIS!","Full Power Energy Wave - Burn out your entire Spirit Gauge in a mighty wave.",true,1.0);
    }

    // DEFAULT PASSIVE
    public PassiveAbility getVirtuousSpirit(){
        return new PassiveAbility("VirtuousSpirit",-1,-1,1.0,1.0,0.9,"Virtuous Spirit - A calm mind makes your Super Attacks cost 0.9x as much.");
    }

    // Super Attack
    public Attack getGalickGun(){
        return new Attack("GalickGun",0,3,5,0,1.0,1.0,1.0,"GALICK GUN!","Galick Gun - Unleash Saiyan Pride with Vegeta's signature wave.",false,0.0);
    }

    // Ultimate Attack
    public Attack getFinalFlash(){
        return new Attack("FinalFlash",0,7,5,0,1.0,1.0,1.0,"FINAL FLASH!","Final Flash - Let loose your entire gauge with Vegeta's finishing move.",true,1.0);
    }

    // Super Attack
    public Attack getBigBangAttack(){
        return new Attack("BigBangAttack",1,1,5,1,1.0,1.0,1.0,"BIG BANG ATTACK!","Big Bang Attack - Launch a ball of exploding Ki.",false,0.0);
    }

    // Ultimate Attack
    public Attack getSuperSpiritBomb(){
        return new Attack("SuperSpiritBomb",5,1,1,1,1.0,1.0,1.0,"I hope you come back someday... As a better person! See you later!","Super Spirit Bomb - Channel energy from across the Server into the ultimate weapon.",true,1.0);
    }

    // Super Attack
    public Attack getMouthBlast(){
        return new Attack("MouthBlast",1,6,5,1,1.0,1.0,1.0,"HAA!","Mouth Blast - Shoots a Blast from (you guessed it) your mouth!",false,0.0);
    }

    // Ultimate Attack
    public Attack getSpecialBeamCannon(){
        return new Attack("SpecialBeamCannon",4,8,5,0,1.0,1.0,1.0,"SPECIAL BEAM CANNON!","Special Beam Cannon - Pierce enemies with Piccolo's signature attack.",true,1.0);
    }

    // Ultimate Attack
    public Attack getCandyBeam(){
        return new Attack("CandyBeam",3,3,5,0,1.0,1.0,1.0,"TURN INTO CHOCOLATE!","Candy Beam - Buu turn you into chocolate and eat you.",true,1.0);
    }

    // Super Attack
    public Attack getKamehameha(){
        return new Attack("Kamehameha",0,2,5,0,1.0,1.0,1.0,"KAME-HAME-HAAAA!","Kamehameha - Unleash the Turtle School's iconic Ki Wave.",false,0.0);
    }

    // Super Attack
    public Attack getBurningAttack(){
        return new Attack("BurningAttack",1,8,5,1,1.0,1.0,1.0,"Burning Attack!","Burning Attack - Protect the future with Trunks' signature blast.",false,0.0);
    }
    
    //Type: 0 = Wave, 1 = Blast, 2 = Disk, 3 = laser, 4 = spiral, 5 = large blast,  6 = barrage, 7 = shield

    //Color: 0 = purple, 1 = white, 2 = blue, 3 = purple, 4 = red, 5 = black, 6 = green, 7 = yellow, 8 = orange

}
