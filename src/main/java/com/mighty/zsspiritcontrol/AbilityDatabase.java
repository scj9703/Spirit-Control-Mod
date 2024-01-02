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
    //Type: 0 = Wave, 1 = Blast, 2 = Disk, 3 = laser, 4 = spiral, 5 = large blast,  6 = barrage, 7 = shield

    //Color: 0 = purple, 1 = white, 2 = blue, 3 = purple, 4 = red, 5 = black, 6 = green, 7 = yellow, 8 = orange

}
