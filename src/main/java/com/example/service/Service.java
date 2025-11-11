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

    // ==== Thêm: client Ollama ====
    // NHỚ: model phải đúng tên bạn đã pull: `docker exec -it ollama ollama list`
    private final OllamaClient bot = new OllamaClient("http://localhost:11434", "llama3.1:8b");

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
                        if (user == null || u.getUserID() != user.getUserID()) {
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
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userID);
                    } else {
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

    // ====== API phụ trợ để gọi bot từ UI ======
    public String askBot(String prompt) throws Exception {
        String system = "Bạn là trợ lý AI, trả lời tiếng Việt tự nhiên, ngắn gọn.";
        return bot.chat(system, prompt, 0.2);
    }
}
