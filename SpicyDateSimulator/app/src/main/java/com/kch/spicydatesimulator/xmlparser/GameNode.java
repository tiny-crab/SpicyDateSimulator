package com.kch.spicydatesimulator.xmlparser;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameNode {
    private String id;
    private List<Response> responses;
    private List<Choice> choices;

    public GameNode(String id) {
        this.id = id;
        this.responses = new ArrayList<>();
        this.choices = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getResponse(HashMap<String, Boolean> flags) {
        Response def = null;
        for (Response r : responses) {
            if (r.showIf() != null) {
                if (flags.containsKey(r.showIf())) {
                    return r.text();
                }
                else
                    continue;
            }
            if (r == null || !r.isDefault())
                def = r;
        }
        if (def == null)
            return "";
        return def.text();
    }

    public String[] getChoices(HashMap<String, Boolean> flags) {
        List<String> res = new ArrayList<>();
        for (Choice c : choices) {
            if (c.showIf() != null && !flags.containsKey(c.showIf())) {
                continue;
            }
            res.add(c.text());
        }
        return (String[])res.toArray();
    }

    public Pair<String, String> makeChoice(HashMap<String, Boolean> flags, int n) {
        List<Choice> res = new ArrayList<>();
        for (Choice c : choices) {
            if (c.showIf() != null && !flags.containsKey(c.showIf())) {
                continue;
            }
            res.add(c);
        }
        try {
            return new Pair<>(res.get(n).getGoTo(), res.get(n).getFlag());
        }
        catch (IndexOutOfBoundsException e) {
            Log.d("GameNode", "Choice selection " + String.valueOf(n) + " for node " + getId() + " is out of bounds");
            e.printStackTrace();
            return null;
        }
    }

    public void addResponse(String text, String showIf, boolean isDefault) {
        responses.add(new Response(text, showIf, isDefault));
    }

    public void addChoice(String text, String goTo, String showIf, String flag) {
        choices.add(new Choice(text, goTo, showIf, flag));
    }

    private class Choice {
        private String text;
        private String goTo;
        private String showIf;
        private String flag;

        public Choice(String text, String goTo, String showIf, String flag) {
            this.text = text;
            this.goTo = goTo;
            this.showIf = showIf;
            this.flag = flag;
        }

        public String getFlag() {
            return flag;
        }

        public String getGoTo() {
            return goTo;
        }

        public String showIf() {
            return showIf;
        }

        public String text() {
            return text;
        }
    }

    private class Response {
        private String text;
        private String showIf;
        private boolean isDefault;

        public Response(String text, String showIf, boolean isDefault) {
            this.text = text;
            this.showIf = showIf;
            this.isDefault = isDefault;
        }

        public Response(String text, String showIf) {
            this(text, showIf, false);
        }

        public Response(String text) {
            this(text, null, false);
        }

        public boolean isDefault() {
            return isDefault;
        }

        public String text() {
            return text;
        }

        public String showIf() {
            return showIf;
        }
    }
}