package com.mighty.zsspiritcontrol.config;

import com.mighty.zsspiritcontrol.SpiritControl;
import com.mighty.zsspiritcontrol.config.reader.AttackReader;
import com.mighty.zsspiritcontrol.config.reader.PassiveReader;

import java.io.File;

public class Config {

    public static Config INSTANCE;

    private final File modConfigDir;
    private File spiritControlDir;
    public Config(File modConfigurationDirectory) {
        modConfigDir = modConfigurationDirectory;
        if(INSTANCE == null)
            INSTANCE = this;
    }

    public void load() {

        if(spiritControlDir == null)
            spiritControlDir = new File(modConfigDir, "spirit_control");

        if(!spiritControlDir.exists())
            spiritControlDir.mkdir();

        try{
            new PassiveReader(new File(spiritControlDir, "passives.json"));
        }catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("Could not load passives: ", e);
        }



        try{
            new AttackReader(new File(spiritControlDir, "attacks.json"), false);
        }catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("Could not load super attacks: ", e);
        }

        try{
            new AttackReader(new File(spiritControlDir, "ultimates.json"), true);
        }catch (Exception e){
            SpiritControl.INSTANCE.LOGGER.warn("Could not load ultimate attacks: ", e);
        }


    }

}
