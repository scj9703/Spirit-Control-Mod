package com.mighty.zsspiritcontrol.ability;

public abstract class AbilityBuilder {
    protected String name = "DEFAULT ABILITY NAME";
    protected String literalId;
    protected String description = "";

    public AbilityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public AbilityBuilder setName(String name){
        this.name = name;
        if(this.literalId == null)
            this.setId(name.replaceAll(" ", ""));
        return this;
    }

    public AbilityBuilder setId(String literalId){
        this.literalId = literalId;
    }

    public abstract Ability getAbility();
}
