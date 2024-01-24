package com.mighty.spiritcontrol.config.reader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class AbilityReader {
    protected File file;

    protected AbilityReader(File file) {
        this.file = file;
    }

    protected void ensureFileExists() throws IOException, URISyntaxException {
        if(!file.exists()) {
            file.createNewFile();
            createExample();
        }
    }


    protected abstract void createExample() throws IOException, URISyntaxException;
}
