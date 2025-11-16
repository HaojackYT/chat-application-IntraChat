package com.example.event;

import com.example.model.ModelLogin;
import com.example.model.ModelRegister;

public interface EventLogin {
    
    public void login(ModelLogin data);
    
    public void register(ModelRegister data, EventMessage message);
    
    public void goLogin();
    
    public void goRegister();
    
}
