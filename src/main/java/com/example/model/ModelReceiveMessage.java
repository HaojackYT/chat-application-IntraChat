package com.example.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelReceiveMessage {
    
    int fromUserID;
    String text;

    public ModelReceiveMessage(int fromUserID, String text) {
        this.fromUserID = fromUserID;
        this.text = text;
    }

    public ModelReceiveMessage(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            fromUserID = obj.getInt("fromUserID");
            text = obj.getString("text");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("fromUserID", fromUserID);
            json.put("text", text);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
