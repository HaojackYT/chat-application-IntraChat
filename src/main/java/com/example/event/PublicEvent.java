package com.example.event;

public class PublicEvent {
    
    private static PublicEvent instance;
    private EventImageView eventImageView;
    private EventChat eventChat;
    
    public static PublicEvent getInstance() {
        if (instance == null) {
            instance = new PublicEvent();
        }
        return instance;
    }
    
    private PublicEvent() {
        
    }
    
    public void addEventImageView(EventImageView eventImageView) {
        this.eventImageView = eventImageView;
    }
    
    public EventImageView getEventImageView() {
        return eventImageView;
    }
    
    public void addEventChat(EventChat eventChat) {
        this.eventChat = eventChat;
    }
    
    public EventChat getEventChat() {
        return eventChat;
    }
}
