package com.kch.spicydatesimulator.download;


import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.util.StringBuilderPrinter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MainDownloadTask implements Callable<ArrayList<Pair<String, String>>> {

    private static final String INDEX_URL = "http://wiederspane1.cs.spu.edu/SpicyDateSimGames";
    private static final String INDEX_FILE = "/gamesList.txt";
    private Context context;

    public MainDownloadTask(Context context) {
        this.context = context;
    }

    /**
     * Downloads the game list file from the server, then proceeds to download all of the games found in the index
     * If a game has been downloaded previously, it will fetch it from the file system instead of downloading it again
     * @return An ArrayList containing pairs where First is the name of the game and Second is the file path containing its data
     */
    @Override
    public ArrayList<Pair<String,String> > call() {
        try {
            URL indexUrl = new URL(INDEX_URL + INDEX_FILE);
            HttpURLConnection con = (HttpURLConnection) indexUrl.openConnection();
            con.setReadTimeout(10000); // 10s read timeout
            con.setConnectTimeout(15000); // 15s connect timeout
            con.connect();
            String[] lines = {};
            // if the connection is good
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                // download into byte[] buffer
                byte[] buffer = new byte[4096];
                while (in.read(buffer) != -1) {} // read download into buffer
                 // read buffer into string array split by newlines
                lines = new String(buffer).split("\\r\\n|\\r|\\n");
                FileOutputStream indexFile = new FileOutputStream(context.getFilesDir()+ INDEX_FILE);
                // and also into a local copy for later offline runs
                indexFile.write(buffer);
                indexFile.close();
                in.close();
            }
            else { // otherwise try to load it from previous save
                File indexFile = new File(context.getFilesDir() + INDEX_FILE);
                if (!indexFile.exists()) { // no connection and no local, abort!
                    return null;
                }
                // this is actually a solution I found
                // like Oracle was looking at their libraries and thought
                // "ya this makes sense, four objects to open a single file"
                // in python this would be
                // file = open("text.txt", "r")
                // BECAUSE THATS HOW EASY THIS SHOULD BE
                // </rant>
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile)));
                // AND HOW ABOUT ONE MORE OBJECT
                // JUST FOR KICKS
                String line;
                ArrayList<String> lineList = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    lineList.add(line);
                }
                lines = (String[]) lineList.toArray();
                br.close();
            }
            for (String l : lines) {
                // file name in [0], name in [1]
                String[] game = l.split(" ", 1);

            }
        }
        catch (MalformedURLException e) {
            Log.d("Download", "Bad url");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("Download", "IOException");
            e.printStackTrace();
        }
        ArrayList<Pair<String,String>> arr = new ArrayList<>();

        return arr;
    }
}