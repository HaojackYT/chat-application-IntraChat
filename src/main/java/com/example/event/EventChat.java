package com.example.event;

import com.example.model.ModelReceiveMessage;
import com.example.model.ModelSendMessage;

public interface EventChat {
    
    public void sendMessage(ModelSendMessage data);
    
    public void receiveMessage(ModelReceiveMessage data);
}
