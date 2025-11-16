package com.example.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelLogin {
    
    String userName;
    String password;

    public ModelLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    public ModelLogin() { }

    public JSONObject toJSONObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userName", userName);
            obj.put("password", password);
            return obj;
        } catch (JSONException e) {
            return null;
        }
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
