package com.example.event;

public class PublicEvent {

    private static PublicEvent instance;
    private EventImageView eventImageView;
    private EventChat eventChat;
    private EventLogin eventLogin;
    private EventMain eventMain;
    private EventMenuLeft eventMenuLeft;

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

    public void addEventLogin(EventLogin eventLogin) {
        this.eventLogin = eventLogin;
    }

    public EventLogin getEventLogin() {
        return eventLogin;
    }

    public void addEventMain(EventMain eventMain) {
        this.eventMain = eventMain;
    }

    public EventMain getEventMain() {
        return eventMain;
    }

    public void addEventMenuLeft(EventMenuLeft eventMenuLeft) {
        this.eventMenuLeft = eventMenuLeft;
    }

    public EventMenuLeft getEventMenuLeft() {
        return eventMenuLeft;
    }
}
