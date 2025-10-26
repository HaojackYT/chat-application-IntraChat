package com.example.event;

import com.example.model.ModelRegister;

public interface EventLogin {
    
    public void login();
    
    public void register(ModelRegister data, EventMessage message);
    
    public void goLogin();
    
    public void goRegister();
    
}
