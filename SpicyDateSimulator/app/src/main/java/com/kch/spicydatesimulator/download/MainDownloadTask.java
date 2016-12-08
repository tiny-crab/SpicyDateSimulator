package com.kch.spicydatesimulator.download;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Pair;
import android.util.StringBuilderPrinter;

import com.kch.spicydatesimulator.MainMenuActivity;

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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class MainDownloadTask implements Runnable {

    private static final String INDEX_URL = "http://wiederspane1.cs.spu.edu/SpicyDateSimGames";
    private static final String INDEX_FILE = "/gamesList.txt";
    private MainMenuActivity context;

    public MainDownloadTask(MainMenuActivity context) {
        this.context = context;
    }

    /**
     * Helper function to test for internet connectivity
     * @return if the app has an internet connection
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Downloads the game list file from the server, then proceeds to download all of the games found in the index
     * If a game has been downloaded previously, it will fetch it from the file system instead of downloading it again
     * @return An ArrayList containing pairs where First is the name of the game and Second is the file path containing its data
     */
    @Override
    public void run() {
        if (!isOnline()) {
            if (context.isRunning())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.fillGameDropDown(null);
                    }
                });
            return;
        }
        try {
            URL indexUrl = new URL(INDEX_URL + INDEX_FILE);
            HttpURLConnection con = (HttpURLConnection) indexUrl.openConnection();
            con.setReadTimeout(3000);
            con.setConnectTimeout(3000);
            con.connect();
            String[] lines = {};
            // if the connection is good
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                // download into byte[] buffer
                byte[] buffer = new byte[2048];
                int bufLen = 0; // keep track of how much data we downloaded
                int read;
                while ((read = in.read(buffer)) != -1) {
                    bufLen += read;
                } // read download into buffer
                 // read buffer into string array split by newlines
                lines = new String(buffer,0,bufLen).split("\\r\\n|\\r|\\n");
                FileOutputStream indexFile = new FileOutputStream(context.getFilesDir()+ INDEX_FILE);
                // and also into a local copy for later offline runs
                indexFile.write(buffer);
                indexFile.close();
                in.close();
            }
            else { // otherwise try to load it from previous save
                File indexFile = new File(context.getFilesDir() + INDEX_FILE);
                if (!indexFile.exists()) { // no connection and no local, abort!
                    return;
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
            con.disconnect();
            ArrayList<Callable<Pair<String,String>>> tasks = new ArrayList<>();
            GameDownloadTask.DIR = context.getFilesDir().getPath();
            for (int i = 0; i < lines.length; i++) { // were skipping the last one because its full of junk data
                // file name in [0], name in [1]
                String l = lines[i];
                String[] game = l.split(" ", 2);
                tasks.add(new GameDownloadTask(context.getFilesDir().getPath(), game[0], game[1]));
            }
            // Triple templates, baby
            List<Future<Pair<String,String>>> games = Executors.newCachedThreadPool().invokeAll(tasks);
            final ArrayList<Pair<String,String>> res = new ArrayList<>(games.size());
            try {
                // process the futures
                for (Future<Pair<String, String>> g : games) {
                    res.add(g.get());
                }
                if (context.isRunning()) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            context.fillGameDropDown(res);
                        }
                    });
                }
            }
            catch (ExecutionException e) {
                e.printStackTrace();
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
        catch (InterruptedException e) {
            Log.d("Download", "InterruptedException");
            e.printStackTrace();
        }
    }
}