package com.mighty.zsspiritcontrol.config.reader;

import java.io.*;
import java.net.URISyntaxException;

public abstract class AbilityReader {
    protected File file;

    protected AbilityReader(File file) throws IOException, URISyntaxException {
        this.file = file;
        if(!file.exists()) {
            file.createNewFile();
            createExample();
        }
    }


    protected abstract void createExample() throws IOException, URISyntaxException;
}
