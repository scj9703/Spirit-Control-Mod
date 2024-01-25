package com.mighty.spiritcontrol.ability;

public abstract class Ability implements Comparable<Ability> {

    protected String literalId;

    protected String name;
    protected String description;

    @Override
    public int compareTo(Ability o){
        return literalId.toLowerCase().compareTo(o.getId().toLowerCase());
    }

    protected Ability(String literalId, String name, String description){
        this.name = name;
        this.literalId = literalId;
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

    public String getId(){
        return this.literalId;
    }

    public String toString(){
        return this.name;
    }
}
