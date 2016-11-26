package com.example.evan.spicydatingsim;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
    private SDSGameSave curSave = null;

    public SDSGame(Reader fileData) {
        createFromStream(fileData);
    }

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
            parser.next();
            // make sure the next one is the <nodes> tag
            parser.require(XmlPullParser.START_TAG, null, NODES_TAG);
            parser.next();
            while (parser.getEventType() == XmlPullParser.START_TAG) {
                parser.require(XmlPullParser.START_TAG, null, NODE_TAG);
                // Currently working here
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

    private class GameNode {
        private String id;

        public GameNode(String id) {
            this.id = id;
        }
    }
}
