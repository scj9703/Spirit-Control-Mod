package com.mighty.spiritcontrol.config.reader;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.config.reader.objects.PassiveJSON;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PassiveReader extends AbilityReader {
    public PassiveReader(File file) throws IOException, URISyntaxException {
        super(file);
        ensureFileExists();

        Gson gson = new Gson();
        Map<String, PassiveJSON> passivesLoaded = gson.fromJson(new FileReader(file), new TypeToken<Map<String, PassiveJSON>>(){}.getType());

        if(passivesLoaded == null){
            return;
        }

        for(String literalId : passivesLoaded.keySet()){
            Ability object = passivesLoaded.get(literalId).getAbility(literalId);
            AbilityDatabase.registerAbility(object);
        }

    }


    @Override
    protected void createExample() throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
        jsonWriter.setIndent("\t");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


        HashMap<String, PassiveJSON> objectHashMap = new HashMap<>();

        PassiveJSON passive = new PassiveJSON();
        passive.setExampleValues();

        objectHashMap.put("example_passive", passive);

        gson.toJson(objectHashMap, objectHashMap.getClass(), jsonWriter);
        jsonWriter.close();

    }
}
