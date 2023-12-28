package com.mighty.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mighty.zsspiritcontrol.zsspiritcontrol;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
/** Extended Player for Spirit Control **/
public class SCPlayer implements IExtendedEntityProperties {
    /**
     * The Player's SC Data.
     */
    public NBTTagCompound data = null;

    /**
     * The player who this data belongs to
     */
    EntityPlayer player = null;

    /**
     * The Spirit Gauge's default max capacity.
     */
    int gaugeCapacity = 1000;

    /**
     * The Spirit Gauge's current capacity
     */
     int currGauge = 0;

    /**
     * Returns the maximum Spirit Gauge Capacity
     * @return int max capacity
     */
    public int getGaugeCapacity() {
        return gaugeCapacity;
    }

    /**
     * Returns the current Spirit Gauge capacity.
     * @return int curr capacity
     */
    public int getCurrGauge(){
        return currGauge;
    }

    /**
     * Prints a display version of the Spirit Gauge. Used for display to players.
     * I.e. {======----} = 69%
     * @return String version of Spirit Gauge.
     */
    public String printGauge(){
        int gauge = getCurrGauge();
        int cap = getGaugeCapacity();
        int oneTenth = cap/10;
        String gaugeString = "{"; // Left Border
        // Displays the 'fullness' of the Spirit Gauge.
        // Every 10% fills the meter's display
        for (int i = 1; i <= 10; i++){
            if (gauge < oneTenth*i){
                gaugeString = gaugeString + "-";
            }
            else {
                gaugeString = gaugeString + "=";
            }
        }
        gaugeString = gaugeString + "}"; // Right Border
        return gaugeString;
    }
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        // Saves the player's SC Data
        return;

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        // Loads the player's SC Data
        return;
    }

    @Override
    public void init(Entity entity, World world) {
    }

    /**
     * @param p The player in question
     * @return The sc properties of player 'p'
     */
    public static SCPlayer getPlayer(EntityPlayer p) {
        SCPlayer ex = (SCPlayer) p.getExtendedProperties(zsspiritcontrol.MODID);

        if (ex.player == null) {
            ex.player = p;
        }

        return ex;
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties("ZSSpiritcontrol", new SCPlayer());
    }
}
