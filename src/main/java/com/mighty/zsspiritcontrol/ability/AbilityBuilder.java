package com.mighty.zsspiritcontrol.ability;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import somehussar.minimessage.MiniMessageParser;

public abstract class AbilityBuilder {
    protected IChatComponent prettyName;
    protected String name = "DEFAULT NAME PLEASE CHANGE";
    protected String literalId;
    protected IChatComponent prettyDescription;
    protected String description = "DEFAULT DESCRIPTION PLEASE CHANGE";

    protected AbilityBuilder() {

    }

    public AbilityBuilder setDescription(String description){
        if(description == null)
            this.description = "";
        else
            this.description = description;
        return this;
    }

    public AbilityBuilder setPrettyDescription(String description){
        this.prettyDescription = MiniMessageParser.getFormat(description);
        return this;
    }
    public AbilityBuilder setPrettyDescription(IChatComponent description) {
        this.prettyDescription = description;
        return this;
    }

    public AbilityBuilder setName(String name){
        if(name == null)
            this.name = "";
        else
            this.name = name;
        return this;
    }
    public AbilityBuilder setPrettyName(IChatComponent name){
        this.prettyName = name;
        return this;
    }

    public AbilityBuilder setPrettyName(String prettyName){
        this.prettyName = MiniMessageParser.getFormat(prettyName);
        return this;
    }

    public AbilityBuilder setId(String literalId){
        this.literalId = literalId;
        return this;
    }

    protected abstract Ability getAbility();

    protected void fixNonPrettyNames(){
        if(this.prettyDescription == null)
            this.prettyDescription = new ChatComponentText(this.description);
        if(this.prettyName == null)
            this.prettyName = new ChatComponentText(this.name);
    }
}
