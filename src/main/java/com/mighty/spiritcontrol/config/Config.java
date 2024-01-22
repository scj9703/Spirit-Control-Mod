package com.mighty.spiritcontrol.config;

import com.mighty.spiritcontrol.SpiritControl;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.config.reader.AttackReader;
import com.mighty.spiritcontrol.config.reader.PassiveReader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    private final String CATEGORY_PASSIVE_GAIN = "SPIRIT_CONTROL_PASSIVE_GAIN";

    public static Config INSTANCE;

    public static double SPIRIT_ON_DAMAGE_DEALT_FLAT;
    public static double SPIRIT_ON_DAMAGE_TAKEN_FLAT;
    public static double SPIRIT_PASSIVE_FLAT;
    private File spiritControlDir;


    public Config(File modConfigurationDirectory) {

        spiritControlDir = new File(modConfigurationDirectory, "spirit_control");
        if(!spiritControlDir.exists())
            spiritControlDir.mkdir();

        loadMainConfig();
        loadAbilities();
        if(INSTANCE == null)
            INSTANCE = this;
    }

    public void loadAbilities() {

        AbilityDatabase.loadDefaults();


        SpiritControl.INSTANCE.LOGGER.info("===REGISTERING CUSTOM PASSIVES===");
        try{
            new PassiveReader(new File(spiritControlDir, "passives.json"));
        }catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("Could not load passives: ", e);
        }
        SpiritControl.INSTANCE.LOGGER.info("===REGISTERING CUSTOM SUPERS===");
        try{
            new AttackReader(new File(spiritControlDir, "attacks.json"), false);
        }catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("Could not load super attacks: ", e);
        }
        SpiritControl.INSTANCE.LOGGER.info("===REGISTERING CUSTOM ULTIMATES===");
        try{
            new AttackReader(new File(spiritControlDir, "ultimates.json"), true);
        }catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("Could not load ultimate attacks: ", e);
        }


    }

    public void loadMainConfig() {
        //Recreated every time because this function can be run multiple times, and it
        //needs to reload the config file to update the changes.
        Configuration mainConfig = new Configuration(new File(spiritControlDir, "main.cfg"));

        SPIRIT_ON_DAMAGE_DEALT_FLAT = mainConfig.getFloat("Spirit gained on damage dealt", CATEGORY_PASSIVE_GAIN, 1f, 0f, 100, "Spirit gained on dealing damage to others (this is the number before passive modifiers)");
        SPIRIT_ON_DAMAGE_TAKEN_FLAT = mainConfig.getFloat("Spirit gained on damage taken", CATEGORY_PASSIVE_GAIN, 1f, 0f, 100, "Spirit gained on taking damage from others (this is the number before passive modifiers)");
        SPIRIT_PASSIVE_FLAT = mainConfig.getFloat("Spirit gained passively", CATEGORY_PASSIVE_GAIN, 0.01f, 0f, 100, "Spirit gained passively (this is the number before passive modifiers)");

        if(mainConfig.hasChanged())
            mainConfig.save();
    }
}
