package com.mighty.spiritcontrol.config.reader;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.mighty.spiritcontrol.ability.Ability;
import com.mighty.spiritcontrol.ability.AbilityDatabase;
import com.mighty.spiritcontrol.config.reader.objects.AttackJSON;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AttackReader extends AbilityReader {

    protected boolean isUltimate;

    public AttackReader(File file, boolean isUltimate) throws IOException, URISyntaxException {
        super(file);
        this.isUltimate = isUltimate;
        ensureFileExists();

        Gson gson = new Gson();
        Map<String, AttackJSON> attacksLoaded = gson.fromJson(new FileReader(file), new TypeToken<Map<String, AttackJSON>>(){}.getType());

        if(attacksLoaded == null){
            return;
        }

        for(String literalId : attacksLoaded.keySet()){
            Ability object = attacksLoaded.get(literalId).getAbility(literalId, isUltimate);
            AbilityDatabase.registerAbility(object);
        }

    }

    @Override
    protected void createExample() throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
        jsonWriter.setIndent("\t");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


        HashMap<String, AttackJSON> objectList = new HashMap<>();

        AttackJSON attack = new AttackJSON();
        attack.setExampleValues();

        objectList.put("example_" + (isUltimate ? "ultimate" : "attack"), attack);

        gson.toJson(objectList, objectList.getClass(), jsonWriter);
        jsonWriter.close();
    }
}
