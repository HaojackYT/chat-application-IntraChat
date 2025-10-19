package com.example.event;

public class PublicEvent {
    
    private static PublicEvent instance;
    private static EventImageView eventImageView;
    
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
}
