package com.example.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelPackageSender {
    
    int fileID;
    String data;
    boolean finish;

    public ModelPackageSender(int fileID, String data, boolean finish) {
        this.fileID = fileID;
        this.data = data;
        this.finish = finish;
    }

    public ModelPackageSender() { }
    
    public JSONObject toJSONPObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("fileID", fileID);
            json.put("data", data);
            json.put("finish", finish);
            return json;
        } catch(JSONException e) {
            return null;
        }
    }
    
    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
