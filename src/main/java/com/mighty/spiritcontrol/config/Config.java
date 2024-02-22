package com.mighty.spiritcontrol.config;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.config.reader.AttackReader;
import com.mighty.spiritcontrol.config.reader.PassiveReader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static Config INSTANCE;

    public static double SPIRIT_ON_DAMAGE_DEALT_FLAT = 1;
    public static double SPIRIT_ON_DAMAGE_TAKEN_FLAT = 1;
    public static double SPIRIT_PASSIVE_FLAT = 0.1;

    public static double DAMAGE_UNIT = 100000;

    //public static boolean ACCEPT_RELEASE = false;
    public static boolean ACCEPT_FUSION = true;
    public static boolean ACCEPT_STAT_BONUSES = true;
    public static boolean ACCEPT_RACIAL_BUFFS = false;
    public static boolean ACCEPT_RACIAL_FORMS = false;
    public static boolean ACCEPT_STATUS_EFF = false;
    public static boolean ACCEPT_NON_RACIAL = false;
    public static boolean ACCEPT_KAIOKEN = false;
    public static double DEATH_PERSIST_PERCENTILE = 1;

    private final File spiritControlDir;


    public Config(File modConfigurationDirectory) {

        spiritControlDir = new File(modConfigurationDirectory, "spirit_control");
        if(!spiritControlDir.exists())
            spiritControlDir.mkdir();

        loadMainConfig();
        loadAbilities();
        if(INSTANCE == null)
            INSTANCE = this;
    }

    /**
     * Attempts to reload all abilities from their respective JSON files.
     */
    public void loadAbilities() {
        AbilityDatabase.loadDefaults();


        SpiritControl.LOGGER.info("===REGISTERING CUSTOM PASSIVES===");
        try{
            new PassiveReader(new File(spiritControlDir, "passives.json"));
        }catch (Exception e){
            SpiritControl.LOGGER.warn("Could not load passives: ", e);
        }
        SpiritControl.LOGGER.info("===REGISTERING CUSTOM SUPERS===");
        try{
            new AttackReader(new File(spiritControlDir, "attacks.json"), false);
        }catch (Exception e){
            SpiritControl.LOGGER.warn("Could not load super attacks: ", e);
        }
        SpiritControl.LOGGER.info("===REGISTERING CUSTOM ULTIMATES===");
        try{
            new AttackReader(new File(spiritControlDir, "ultimates.json"), true);
        }catch (Exception e){
            SpiritControl.LOGGER.warn("Could not load ultimate attacks: ", e);
        }


    }

    /**
     * Reads the config;
     */
    public void loadMainConfig() {
        //Recreated every time because this function can be run multiple times, and it
        //needs to reload the config file to update the changes.
        Configuration mainConfig = new Configuration(new File(spiritControlDir, "main.cfg"));

        String CATEGORY_PASSIVE_GAIN = "SPIRIT_CONTROL_PASSIVE_GAIN";
        String CATEGORY_DAMAGE = "SPIRIT_DAMAGE";
        String CATEGORY_STAT_CALCULATIONS = "DAMAGE_TOGGLES";
        String CATEGORY_DEATH = "DEATH";

        DEATH_PERSIST_PERCENTILE = mainConfig.getFloat("Spirit death persistence percentile", CATEGORY_DEATH, 25, 0, 100, "Percentile amount of how much Spirit a player keeps after death") / 100.0;

        DAMAGE_UNIT = mainConfig.getFloat("Base unit used for calculating damage", CATEGORY_DAMAGE, 1000000, 1, 500000000, "Formula: (effectiveLevel / MaxLevel) * (damageUnit * attackDamageModifier), where effectiveLevel is a level derived from stats ");

        SPIRIT_ON_DAMAGE_DEALT_FLAT = mainConfig.getFloat("Spirit gained on damage dealt", CATEGORY_PASSIVE_GAIN, 1f, 0f, 100, "Spirit gained on dealing damage to others (this is the number before passive modifiers)");
        SPIRIT_ON_DAMAGE_TAKEN_FLAT = mainConfig.getFloat("Spirit gained on damage taken", CATEGORY_PASSIVE_GAIN, 1f, 0f, 100, "Spirit gained on taking damage from others (this is the number before passive modifiers)");
        SPIRIT_PASSIVE_FLAT = mainConfig.getFloat("Spirit gained passively", CATEGORY_PASSIVE_GAIN, 0.01f, 0f, 100, "Spirit gained passively (this is the number before passive modifiers)");

        ACCEPT_FUSION = mainConfig.getBoolean("Fusion", CATEGORY_STAT_CALCULATIONS, true, "True - uses fusion stats, False - player stats");
        ACCEPT_STAT_BONUSES = mainConfig.getBoolean("Bonus attributes", CATEGORY_STAT_CALCULATIONS, true, "Should use jrmcabonus / DBCA");
        ACCEPT_RACIAL_BUFFS = mainConfig.getBoolean("Racial buffs", CATEGORY_STAT_CALCULATIONS, false, "Arco powerpoints or majin absorption");
        ACCEPT_STATUS_EFF = mainConfig.getBoolean("Status Effects", CATEGORY_STAT_CALCULATIONS, false, "Majin/Legendary");
        ACCEPT_RACIAL_FORMS = mainConfig.getBoolean("Racial forms", CATEGORY_STAT_CALCULATIONS, false, null);
        ACCEPT_NON_RACIAL = mainConfig.getBoolean("Non racial forms", CATEGORY_STAT_CALCULATIONS, false, "Non racial forms like KK, Mystic, UI or GoD");
        ACCEPT_KAIOKEN = mainConfig.getBoolean("Kaioken", CATEGORY_STAT_CALCULATIONS, false, null);

        if(mainConfig.hasChanged())
            mainConfig.save();
    }
}
