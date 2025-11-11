package com.example.model;

import com.example.app.MessageType;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelReceiveMessage {

    private MessageType messageType;
    int fromUserID;
    String text;
    private ModelReceiveImage dataImage;

    public ModelReceiveMessage(MessageType messageType, int fromUserID, String text) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.text = text;
    }

    public ModelReceiveMessage() {
    }

    public ModelReceiveMessage(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            messageType = messageType.toMessageType(obj.getInt("messageType"));
            fromUserID = obj.getInt("fromUserID");
            text = obj.getString("text");
            if (!obj.isNull("dataImage")) {
                dataImage = new ModelReceiveImage(obj.get("dataImage"));
            }
        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("messageType", messageType.getValue());
            json.put("fromUserID", fromUserID);
            json.put("text", text);
            if (dataImage != null) {
                json.put("dataImage", dataImage.toJSONObject());
            }
            return json;
        } catch (JSONException e) {
            return null;
        }
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
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

    public ModelReceiveImage getDataImage() {
        return dataImage;
    }

    public void setDataImage(ModelReceiveImage dataImage) {
        this.dataImage = dataImage;
    }

}
