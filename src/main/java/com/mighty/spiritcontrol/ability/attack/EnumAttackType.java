package com.mighty.spiritcontrol.ability.attack;

import com.mighty.spiritcontrol.SpiritControl;

public enum EnumAttackType {
    WAVE(0),
    BLAST(1),
    DISK(2),
    LASER(3),
    SPIRAL(4),
    BIG_BLAST(5),
    BARRAGE(6),
    SHIELD(7),
    EXPLOSION(8);

    private final int id;

    EnumAttackType(int id){
        this.id = id;
    }

    public EnumAttackType getValueByName(String name){
        try{
            return valueOf(name.toUpperCase());
        } catch (Exception e){
            SpiritControl.LOGGER.warn("COULD NOT GET ATTACK TYPE: "+name+". REVERTING TO BLAST");
            return EnumAttackType.BLAST;
        }
    }

    public int getValue(){
        return this.id;
    }
}
