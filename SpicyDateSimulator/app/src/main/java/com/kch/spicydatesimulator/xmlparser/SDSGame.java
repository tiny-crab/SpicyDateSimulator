package com.kch.spicydatesimulator.xmlparser;

import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

/**
 * A Game object in SpicyDateSimulator
 * Contains all of the information in a game, including the whole conversation tree
 */

public class SDSGame {
    private String gameName;
    private String id;
    private String desc;
    private String imgid;
    private HashMap<String, GameNode> gameMap;
    private GameNode curNode = null;
    private GameNode startingNode = null;
    private SDSGameSave curSave = null;

    public SDSGame(Reader fileData, SDSGameSave save) {
        gameMap = new HashMap<>();
        createFromStream(fileData);
        if (save == null) {
            curSave = new SDSGameSave();
        }
        else {
            curSave = save;
        }
        startingNode = curNode;
    }

    public String getResponse() {
        if (curNode != null) {
            return curNode.getResponse(curSave.getFlagMap());
        }
        else {
            Log.d("SDSGame", "No start node given");
            return null;
        }
    }

    public String[] getChoices() {
        if (curNode != null) {
            return curNode.getChoices(curSave.getFlagMap());
        }
        else {
            Log.d("SDSGame", "No start node given");
            return null;
        }
    }


    public boolean makeChoice(int index) {
        Pair<String, String> newNodeInfo = curNode.makeChoice(curSave.getFlagMap(), index);
        String newNodeId = newNodeInfo.first;
        String newFlag = newNodeInfo.second;
        if (curSave != null) {
            curSave.setFlag(newFlag);
        }
        if (newNodeId == null || newNodeId.equals(GOTO_END)) {
            curNode = null;
            return false;
        }
        else {
            curNode = gameMap.get(newNodeId);
            if (curNode == null) {
                Log.e("SDSGame", "Error: No node with id " + newNodeId + " found");
            }
            else if (curSave != null) {
                curSave.updateCurNode(curNode.getId());
            }
            return true;
        }
    }

    public void restart() {
        curNode = startingNode;
    }

    // private stuff below this

    private void createFromStream(Reader fileData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(fileData);
            parser.require(XmlPullParser.START_DOCUMENT, null, null);
            parser.next();
            // make sure it starts with a <game> tag
            parser.require(XmlPullParser.START_TAG, null, GAME_TAG);
            desc = parser.getAttributeValue(null, GAME_DESC);
            gameName = parser.getAttributeValue(null, GAME_NAME);
            id = parser.getAttributeValue(null, GAME_ID);
            imgid = parser.getAttributeValue(null, GAME_IMG_ID);
            parser.next();
            parser.next();
            // make sure the next one is the <nodes> tag
            parser.require(XmlPullParser.START_TAG, null, NODES_TAG);
            parser.nextTag();
            while (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(NODE_TAG)) {
                parser.require(XmlPullParser.START_TAG, null, NODE_TAG);
                GameNode newNode = new GameNode(parser.getAttributeValue(null, NODE_ID));
                if (parser.getAttributeValue(null, NODE_START) != null) {
                    curNode = newNode;
                }
                parser.nextTag();
                while(parser.getName().equals(RESPONSE_TAG)) {
                    String showIf = parser.getAttributeValue(null, NODE_SHOWIF);
                    boolean def = parser.getAttributeValue(null, NODE_DEFAULT) != null;
                    parser.next();
                    parser.require(XmlPullParser.TEXT, null, null);
                    String text = parser.getText();
                    newNode.addResponse(text, showIf, def);
                    parser.nextTag();
                    parser.require(XmlPullParser.END_TAG, null, RESPONSE_TAG);
                    parser.nextTag();
                }
                while(parser.getName().equals(CHOICE_TAG)) {
                    String showIf = parser.getAttributeValue(null, NODE_SHOWIF);
                    String goTo = parser.getAttributeValue(null, NODE_GOTO);
                    String flag = parser.getAttributeValue(null, NODE_SETFLAG);
                    parser.next();
                    parser.require(XmlPullParser.TEXT, null, null);
                    String text = parser.getText();
                    newNode.addChoice(text, goTo, showIf, flag);
                    parser.nextTag();
                    parser.require(XmlPullParser.END_TAG, null, CHOICE_TAG);
                    parser.nextTag();
                }
                parser.require(XmlPullParser.END_TAG, null, NODE_TAG);
                gameMap.put(newNode.getId(), newNode);
                parser.nextTag();
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // xml file attribute consts
    private static final String GAME_TAG = "game";
    private static final String NODE_TAG = "node";
    private static final String NODES_TAG = "nodes";
    private static final String RESPONSE_TAG = "response";
    private static final String CHOICE_TAG = "choice";

    private static final String GAME_NAME = "name";
    private static final String GAME_ID = "id";
    private static final String GAME_DESC = "description";
    private static final String GAME_IMG_ID = "imgid";


    private static final String NODE_GOTO = "goTo";
    private static final String NODE_DEFAULT = "default";

    private static final String NODE_SETFLAG = "setFlag";
    private static final String NODE_SHOWIF = "showIf";
    private static final String NODE_START = "start";
    private static final String NODE_ID = "id";

    private static final String GOTO_END = "_end";


}