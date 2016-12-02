package com.kch.spicydatesimulator.download;

import java.util.concurrent.Callable;

/**
 * Created by evan on 11/30/2016.
 */

public class GameDownloadTask implements Callable<String> {
    private String gameUrl;

    public GameDownloadTask(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    @Override
    public String call() throws Exception {

        return null;
    }
}
