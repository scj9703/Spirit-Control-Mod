package com.mighty.zsspiritcontrol.ability;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class Ability {

    protected String literalId;

    protected IChatComponent name;
    protected IChatComponent description;

    protected Ability(String literalId, String name, String description){
        this.name = new ChatComponentText(name);
        this.literalId = literalId;
        this.description = new ChatComponentText(description);
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
        return this.description.getFormattedText();
    }

    /**
     * @return Ability's name as a string.
     */
    public IChatComponent getName(){
        return this.name;
    }

    public String getId(){
        return this.literalId;
    }

    public String toString(){
        return this.name.getFormattedText();
    }
}
