package com.mighty.zsspiritcontrol.ability;

public class Ability {

    protected String name;
    protected String description;

    protected Ability(String name, String description){
        this.name = name;
        this.description = description;
    }

    /**
     * @return Ability's description as a string.
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * @return Ability's name as a string.
     */
    public String getName(){
        return this.name;
    }

    public String toString(){
        return this.name;
    }
}
