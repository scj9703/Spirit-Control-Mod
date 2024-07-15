package com.mighty.spiritcontrol.ability;

public abstract class AbilityBuilder {
    protected String name;
    protected String literalId;

    protected String description = "DEFAULT DESCRIPTION PLEASE CHANGE";

    protected AbilityBuilder() {

    }

    public AbilityBuilder setDescription(String description){
        if(description == null)
            this.description = "";
        else
            this.description = description.replace("&", "\u00a7");
        return this;
    }

    public AbilityBuilder setName(String name){
        if(name == null)
            this.name = "";
        else
            this.name = name.replace("&", "\u00a7");
        return this;
    }

    public AbilityBuilder setId(String literalId){
        if(this.name == null){
            this.name = literalId;
        }
        this.literalId = literalId;
        return this;
    }

    protected abstract Ability getAbility();
}
