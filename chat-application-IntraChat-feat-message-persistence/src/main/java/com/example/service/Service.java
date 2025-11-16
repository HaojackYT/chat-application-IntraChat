package com.example.service;

import com.example.event.PublicEvent;
import com.example.model.ModelReceiveMessage;
import com.example.model.ModelUserAccount;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "localhost";
    private ModelUserAccount user;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private Service() { }

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
                    if (os.length > 0) {
                        try {
                            ModelReceiveMessage message = new ModelReceiveMessage(os[0]);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    PublicEvent.getInstance().getEventChat().receiveMessage(message);
                                }
                            });
                        } catch (Exception e) {
                            error(e);
                        }
                    }
                }
            });
            
            client.open();
        } catch (URISyntaxException e) {
            error(e);
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
    
    public void requestHistory(int friendID) {
        if (client != null && client.connected()) {
            // Sử dụng Ack để nhận dữ liệu hồi đáp từ Server
            client.emit("get_history", friendID, new Ack() {
                @Override
                public void call(Object... args) {
                    if (args.length > 0 && args[0] != null) {
                        PublicEvent.getInstance().getEventChat().loadHistory(args[0]);
                    }
                }
            });
        } else {
             error(new Exception("Socket is not connected. Cannot request history."));
        }
    }
}