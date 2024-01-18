package com.mighty.zsspiritcontrol.ability.passive;

import com.mighty.zsspiritcontrol.SpiritControl;

public enum EnumFillMethod {
    DAMAGE_TAKEN,
    DAMAGE_DEALT,
    PASSIVE;

    public EnumFillMethod getValueByName(String name){
        try{
            return valueOf(name.toUpperCase());
        } catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("COULD NOT GET FILL METHOD: "+name+". REVERTING TO PASSIVE");
            return EnumFillMethod.PASSIVE;
        }
    }
}
