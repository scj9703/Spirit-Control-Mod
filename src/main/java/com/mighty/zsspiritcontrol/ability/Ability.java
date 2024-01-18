package com.mighty.zsspiritcontrol.ability;

public abstract class Ability {

    protected String name;
    protected String literalId;
    protected String description;

    protected Ability(String literalId, String name, String description){
        this.name = name;
        this.literalId = literalId;
        this.description = description;
    }

    /**
     * @deprecated
     */
    protected Ability(String name, String description){
        this(name.replaceAll(" ", ""), name, description);

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

    public String getId(){
        return this.literalId;
    }

    public String toString(){
        return this.name;
    }
}
