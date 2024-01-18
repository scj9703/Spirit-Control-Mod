package com.mighty.zsspiritcontrol.ability;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class AbilityBuilder {
    protected IChatComponent name = new ChatComponentText("DEFAULT ABILITY NAME");
    protected String literalId;
    protected IChatComponent description = new ChatComponentText("");

    public AbilityBuilder setDescription(IChatComponent description) {
        this.description = description;
        return this;
    }

    public AbilityBuilder setName(IChatComponent name){
        this.name = name;
        if(this.literalId == null)
            this.setId(name.getUnformattedText().replaceAll(" ", ""));
        return this;
    }

    public AbilityBuilder setId(String literalId){
        this.literalId = literalId;
        return this;
    }

    public abstract Ability getAbility();
}
