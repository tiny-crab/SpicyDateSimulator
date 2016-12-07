package com.kch.spicydatesimulator.download;

import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * Created by evan on 11/30/2016.
 */

public class GameDownloadTask implements Callable<Pair<String,String>> {
    private static final String INDEX_URL = "http://wiederspane1.cs.spu.edu/SpicyDateSimGames";
    public static String DIR; // set by MainDownloadTask


    private String gameName;
    private String dirPath;
    private String gameFile;

    public GameDownloadTask(String dirPath, String gameFile, String gameName) {
        this.dirPath = dirPath;
        this.gameName = gameName;
        this.gameFile = gameFile;
    }

    @Override
    public Pair<String,String> call() throws Exception {
        File gameFile = new File(this.dirPath + "/" + this.gameFile);
        if (gameFile.exists()) { // we got it locally, load it from there
            return new Pair<>(this.gameFile,this.gameName);
        }
        // try the internet!
        try {
            URL indexUrl = new URL(INDEX_URL + "/" + this.gameFile);
            HttpURLConnection con = (HttpURLConnection) indexUrl.openConnection();
            con.setReadTimeout(10000); // 10s read timeout
            con.setConnectTimeout(15000); // 15s connect timeout
            con.connect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                // download into byte[] buffer
                Scanner scanner = new Scanner(in);

                byte[] buffer = new byte[4096];
                int bufLen = 0; // keep track of how much data we downloaded
                int read;
                FileOutputStream indexFile = new FileOutputStream(DIR+ "/" + this.gameFile);
                PrintWriter printer = new PrintWriter(indexFile);
                while (scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    printer.write(s);
                }
                printer.flush();
                printer.close();
                indexFile.close();
                scanner.close();
//                while ((read = in.read(buffer)) != -1) {
//                    bufLen += read;
//                    indexFile.write(buffer, bufLen, read);
//                } // read download into buffer
                // read buffer into string array split by newlines

                // and also into a local copy for later offline runs
//                    indexFile.write(buffer);
//                indexFile.close();
//                in.close();
                con.disconnect();
                return new Pair<>(this.gameFile, this.gameName);
            }
        }
        catch (Exception e) { // either no internet connection or file not found
            return null;
        }
        return null;
    }
}
