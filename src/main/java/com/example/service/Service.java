package com.example.service;

import com.example.event.PublicEvent;
import com.example.model.ModelFileSender;
import com.example.model.ModelReceiveMessage;
import com.example.model.ModelSendMessage;
import com.example.model.ModelUserAccount;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "localhost";
    private ModelUserAccount user;
    private List<ModelFileSender> fileSender;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private Service() {
        fileSender = new ArrayList<>();
    }

    public void startClient() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);
            
            client.on("list_user", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    List<ModelUserAccount> users = new ArrayList<>();
                    for (Object o : os) {
                        ModelUserAccount u = new ModelUserAccount(o);
                        if (u.getUserID() != user.getUserID()) {
                            users.add(u);
                        }
                    }
                    PublicEvent.getInstance().getEventMenuLeft().newUser(users);
                }
            });
            
            client.on("user_status", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int userID = (Integer) os[0];
                    boolean status = (Boolean) os[1];
                    if (status) {
                        // Connect
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userID);
                    } else {
                        // Disconnect
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userID);
                    }
                }
            });
            
            client.on("receive_ms", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    ModelReceiveMessage message = new ModelReceiveMessage(os[0]);
                    PublicEvent.getInstance().getEventChat().receiveMessage(message);
                }
            });
            
            client.open();
        } catch (URISyntaxException e) {
            error(e);
        }
    }
    
    public ModelFileSender addFile(File file, ModelSendMessage message) throws IOException {
        ModelFileSender data = new ModelFileSender(file, client, message);
        message.setFile(data);
        fileSender.add(data);
        // For send file one by one
        if (fileSender.size() == 1) {
            data.initSend();
        }
        return data;
    }
    
    public void fileSendFinish(ModelFileSender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()) {
            // Start send new file when old file finish sending
            fileSender.get(0).initSend();
        }
    }

    public Socket getClient() {
        return client;
    }
    
    private void error(Exception e) {
        System.out.println(e);
    }

    public ModelUserAccount getUser() {
        return user;
    }

    public void setUser(ModelUserAccount user) {
        this.user = user;
    }
}
