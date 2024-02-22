package com.mighty.spiritcontrol.ability.attack;

import JinRyuu.JRMCore.JRMCoreConfig;
import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.config.Config;
import com.mighty.spiritcontrol.player.SCPlayer;
import net.minecraft.server.MinecraftServer;
import somehussar.minimessage.MMParser;

public class Attack extends Ability {

    private final byte type;
    private final byte color;
    private final int speed;
    private final int density;
    private final boolean effect;
    private final double dmgModifier;
    private final double costModifier;
    private final double casttime;
    private final double cooldown;
    private final String fireMessage;
    private final boolean isUltimate;
    private final double fatigue;

    Attack(String id, String name, String description, byte type, byte color, String fireMessage, int speed, int density, boolean effect, double dmgModifier, double cost, double cooldown, double casttime, boolean isUltimate, double fatigue){
        super(id, name, description);
        this.type = type;
        this.color = color;
        this.speed = speed;
        this.density = density;
        this.effect = effect;
        this.dmgModifier = dmgModifier;
        this.costModifier = cost;
        this.cooldown = cooldown;
        this.casttime = casttime;
        this.fireMessage = fireMessage;
        this.isUltimate = isUltimate;
        this.fatigue = fatigue;

    }

    public byte getType() {
        return type;
    }

    public byte getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isEffect() {
        return effect;
    }

    public double getDmgModifier() {
        return dmgModifier;
    }

    public double getCostModifier() {
        return costModifier;
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getCasttime() {
        return casttime;
    }

    public String getFireMessage() {
        return fireMessage;
    }

    public boolean isUltimate() {
        return isUltimate;
    }

    public double getFatigue() {
        return fatigue;
    }

    /**
     * Fires a ki attack making the player its source.
     * 
     * A ki attack drains the player according to this formula : MaxBaseSpirit * CostModifier
     * @param ex player to make shoot
     */
    public void fire(SCPlayer ex) {
        if(!fireMessage.isEmpty())
            ex.addChatMessage(MMParser.getFormat("<aqua>==> <white>"+this.getFireMessage()));
        ex.setCooldown(cooldown);

        if(fatigue > 0 && !ex.isFatigued())
            ex.setFatigue(fatigue);

        //(effectiveLevel / maxLevel) * (damageUnit * attackDamageModifier)
        int damage = getDamage(ex);

        ex.removeSpiritByPassive(ex.getMaxBaseSpirit() * getCostModifier());
        MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "dbcspawnki "+type+" "+speed+" "+damage+" "+(effect ? 1 : 0)+" "+color+" "+density+" 1 10 0 0 0 "+ex.player.getCommandSenderName());
        ex.dbcPlayer.getNbt().setByte("jrmcFrng", (byte) 1); //Fixes a bug with ki attacks not being connected to the player
        ex.addChatMessage(ex.drawPrettyGauge());
    }

    private int getDamage(SCPlayer ex) {
        double damageScaling = (double) ex.dbcPlayer.getEffectiveLevel() / ((double) (JRMCoreConfig.tmx * 6) /5-11);
        int damage = (int) (damageScaling * (dmgModifier * Config.DAMAGE_UNIT) / 2);  // divided by 2 because we use 100% charge which already gives double damage.

        damage = (int) (damage / JRMCoreConfig.dat5696[type][1]); //Removes a quirky issue with DBC using its own config to scale the damage
        return damage;
    }
}
