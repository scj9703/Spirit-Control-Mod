package com.mighty.spiritcontrol.ability.attack;

import com.mighty.spiritcontrol.SpiritControl;

public enum EnumAttackColor {
    ALIGNMENT_BASED(0),
    WHITE(1),
    BLUE(2),
    PURPLE(3),
    RED(4),
    BLACK(5),
    GREEN(6),
    YELLOW(7),
    ORANGE(8),
    PINK(9),
    MAGENTA(10),
    LIGHT_PINK(11),
    CYAN(12),
    DARK_CYAN(13),
    LIGHT_CYAN(14),
    DARK_GRAY(15),
    GRAY(16),
    DARK_BLUE(17),
    LIGHT_BLUE(18),
    DARK_PURPLE(19),
    LIGHT_PURPLE(20),
    DARK_RED(21),
    LIGHT_RED(22),
    DARK_GREEN(23),
    LIME(24),
    DARK_YELLOW(25),
    LIGHT_YELLOW(26),
    GOLD(27),
    LIGHT_ORANGE(28),
    DARK_BROWN(29),
    LIGHT_BROWN(30);
    private final int color;

    EnumAttackColor(int color) {
        this.color = color;
    }

    public EnumAttackColor getValueByName(String name){
        try{
            return valueOf(name.toUpperCase());
        } catch (Exception e){
            SpiritControl.LOGGER.warn("COULD NOT GET COLOR: "+name+". REVERTING TO ALIGNMENT_BASED");
            return EnumAttackColor.ALIGNMENT_BASED;
        }
    }

    public int getValue(){
        return this.color;
    }
}
