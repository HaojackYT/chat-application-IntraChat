package com.example.service;

import com.example.app.MessageType;
import com.example.event.EventChat;
import com.example.event.EventFileReceiver;
import com.example.event.PublicEvent;
import com.example.model.ModelFileReceiver;
import com.example.model.ModelFileSender;
import com.example.model.ModelReceiveMessage;
import com.example.model.ModelSendMessage;
import com.example.model.ModelUserAccount;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "localhost";
    private ModelUserAccount user;
    private List<ModelFileSender> fileSender;
    private List<ModelFileReceiver> fileReceiver;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private Service() {
        fileSender = new ArrayList<>();
        fileReceiver = new ArrayList<>();
    }

    public void startClient() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);

            // Nhận danh sách user
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

            // Cập nhật trạng thái user (online/offline)
            client.on("user_status", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int userID = (Integer) os[0];
                    boolean status = (Boolean) os[1];
                    if (status) {
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userID);
                    } else {
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userID);
                    }
                }
            });

            // Nhận tin nhắn mới
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

        if (fileSender.size() == 1) {
            data.initSend();
        }
        return data;
    }

    public void fileSendFinish(ModelFileSender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()) {
            fileSender.get(0).initSend();
        }
    }

    public void fileReceiveFinish(ModelFileReceiver data) throws IOException {
        fileReceiver.remove(data);
        if (!fileReceiver.isEmpty()) {
            fileReceiver.get(0).initReceive();
        }
    }

    public void addFileReceiver(int fileID, EventFileReceiver event) throws IOException {
        ModelFileReceiver data = new ModelFileReceiver(fileID, client, event);
        fileReceiver.add(data);
        if (fileReceiver.size() == 1) {
            data.initReceive();
        }
    }

    // REQUEST HISTORY
    public void requestHistory(int toUserID, EventChat callback) {
        if (client == null || !client.connected()) return;

        client.emit("requestHistory", new Object[]{toUserID}, (Object... args) -> {
    if (args.length == 0) return;

    JSONObject obj = new JSONObject(args[0]);
    boolean action = obj.optBoolean("action", false);

    if (!action) {
        System.out.println("Server returned action=false: " + obj.optString("message"));
        return;
    }

    JSONArray messageList = obj.optJSONArray("messageList");
    if (messageList == null) return;

    for (int i = 0; i < messageList.length(); i++) {
        JSONObject msgObj = messageList.getJSONObject(i);

        int messageType = msgObj.optInt("messageType");
        int fromUserID = msgObj.optInt("fromUserID");
        String text = msgObj.optString("text");

        ModelReceiveMessage msg = new ModelReceiveMessage(
    MessageType.toMessageType(messageType), // convert int -> MessageType
    fromUserID,
    text,
    null
);
        PublicEvent.getInstance().getEventChat().receiveMessage(msg);
    }
});
    }

    public Socket getClient() {
        return client;
    }

    private void error(Exception e) {
        e.printStackTrace();
    }

    public ModelUserAccount getUser() {
        return user;
    }

    public void setUser(ModelUserAccount user) {
        this.user = user;
    }
}
