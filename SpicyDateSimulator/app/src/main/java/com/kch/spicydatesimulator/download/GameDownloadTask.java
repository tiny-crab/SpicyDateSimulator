package com.kch.spicydatesimulator.download;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by evan on 11/30/2016.
 */

public class GameDownloadTask implements Callable<String> {
    private String gameName;
    private String dirPath;

    public GameDownloadTask(String dirPath, String gameName) {
        this.dirPath = dirPath;
        this.gameName = gameName;
    }

    @Override
    public String call() throws Exception {
        File gameFile = new File(dirPath + "/" + gameName);
        if (gameFile.exists()) { // no connection and no local, abort!
            return
        }
        return null;
    }
}
