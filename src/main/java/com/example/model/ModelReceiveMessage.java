package com.example.model;

import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;

public class ModelReceiveMessage {
    
    int fromUserID;
    String text;
    private int toUserID;

    public ModelReceiveMessage(int fromUserID, int toUserID, String text, Timestamp sentTime) { // ✅ THÊM sentTime
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.text = text;
    }

    public ModelReceiveMessage(int fromUserID, int toUserID, String text) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.text = text;
    }

    public ModelReceiveMessage(Object json) {
        if (json instanceof JSONObject) {
            JSONObject obj = (JSONObject) json;
            try {
                // Trường hợp 1: LỊCH SỬ CHAT (Ack Response từ Server. Dùng key 'senderId')
                if (obj.has("senderId")) {
                    this.fromUserID = obj.getInt("senderId");
                    this.toUserID = obj.getInt("receiverId"); 
                    this.text = obj.getString("content");
                    
                // Trường hợp 2: REAL-TIME (Event "receive_ms". Dùng key 'fromUserID')
                } else if (obj.has("fromUserID") && obj.has("text")) {
                    this.fromUserID = obj.getInt("fromUserID");
                    this.toUserID = obj.getInt("toUserID"); 
                    this.text = obj.getString("text");
                }
            } catch (Exception e) {
                System.err.println("Error parsing ModelReceiveMessage from JSON: " + e.getMessage());
            }
        }
    }
    
    public ModelReceiveMessage() { }
    
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
