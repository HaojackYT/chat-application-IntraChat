package com.example.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelReceiveMessage {
    
    int fromUserID;
    String text;
    private int toUserID;

    public ModelReceiveMessage(int fromUserID, String text) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.text = text;
    }

    public ModelReceiveMessage(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            fromUserID = obj.getInt("fromUserID");
            if (obj.has("toUserID")) {
                toUserID = obj.getInt("toUserID");
            }
            text = obj.getString("text");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("fromUserID", fromUserID);
            if (toUserID != 0) {
                json.put("toUserID", toUserID);
            }
            json.put("text", text);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }

    public int getToUserID() {
    return toUserID;
    }

    public void setToUserID(int toUserID) {
    this.toUserID = toUserID;
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
