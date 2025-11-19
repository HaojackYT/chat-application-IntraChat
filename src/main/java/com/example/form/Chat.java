package com.example.form;

import com.example.component.ChatBody;
import com.example.component.ChatBottom;
import com.example.component.ChatTitle;
import com.example.event.EventChat;
import com.example.event.PublicEvent;
import com.example.model.ModelReceiveMessage;
import com.example.model.ModelSendMessage;
import com.example.model.ModelUserAccount;
import com.example.service.Service;
import net.miginfocom.swing.MigLayout;
import java.util.List;

public class Chat extends javax.swing.JPanel {

    private ChatTitle chatTitle;
    private ChatBody chatBody;
    private ChatBottom chatBottom;

    public Chat() {
        initComponents();
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx", "0[fill]0", "0[]0[100%, fill]0[shrink 0]0"));
        chatTitle = new ChatTitle();
        chatBody = new ChatBody();
        chatBottom = new ChatBottom();

        // Thêm listener cho chat realtime
        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void sendMessage(ModelSendMessage data) {
                // Hiển thị tin nhắn từ chính mình
                if (chatBottom.getUser() != null &&
                        data.getToUserID() == chatBottom.getUser().getUserID()) {
                    chatBody.addItemRight(data);
                }
            }

            @Override
            public void receiveMessage(ModelReceiveMessage data) {
                // Hiển thị tin nhắn từ user hiện tại
                if (chatBottom.getUser() != null &&
                        data.getFromUserID() == chatBottom.getUser().getUserID()) {
                    chatBody.addItemLeft(data);
                }
            }
        });

        add(chatTitle, "wrap");
        add(chatBody, "wrap");
        add(chatBottom, "h ::50%");
    }

    // Set user khi chọn chat mới
    public void setUser(ModelUserAccount user) {
        chatTitle.setUserName(user);
        chatBottom.setUser(user);
        chatBody.clearChat();
        loadHistory(user); // Gọi load lịch sử khi chọn user
    }

    // Load lịch sử tin nhắn từ server
    private void loadHistory(ModelUserAccount user) {
        if (user == null) return;
        chatBody.clearChat();
        chatBottom.setUser(user);

        Service.getInstance().requestHistory(user.getUserID(), new EventChat() {
            @Override
            public void sendMessage(ModelSendMessage data) {
                // Không cần xử lý
            }

            @Override
            public void receiveMessage(ModelReceiveMessage data) {
                // Thêm tin nhắn nhận được vào ChatBody
                chatBody.addItemLeft(data);
            }
        });
    }

    public void updateUser(ModelUserAccount user) {
        chatTitle.updateUser(user);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 727, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );
    }
}
