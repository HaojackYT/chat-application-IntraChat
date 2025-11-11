package com.example.form;

import com.example.component.ChatBody;
import com.example.component.ChatBottom;
import com.example.component.ChatTitle;
import com.example.event.EventChat;
import com.example.event.PublicEvent;
import com.example.model.ModelReceiveMessage;
import com.example.model.ModelSendMessage;
import com.example.model.ModelUserAccount;
import net.miginfocom.swing.MigLayout;

// Nếu dự án có enum MessageType, import và set TEXT cho chắc
import com.example.app.MessageType;

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

        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void sendMessage(ModelSendMessage data) {
                // Hiện tin user ở bên phải
                chatBody.addItemRight(data);

                // Kiểm tra lệnh /bot
                String text = data.getText() == null ? "" : data.getText().trim();
                final String BOT_PREFIX = "/bot ";

                if (text.toLowerCase().startsWith(BOT_PREFIX)) {
                    String ask = text.substring(BOT_PREFIX.length()).trim();
                    if (ask.isEmpty()) return;

                    // (Nếu app ở container: đổi localhost -> host.docker.internal)
                    final String BASE_URL = "http://localhost:11434";
                    final String MODEL    = "llama3.1:8b";

                    // Gọi bot nền (không block UI)
                    new javax.swing.SwingWorker<String, Void>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            String system = "Bạn là trợ lý AI, trả lời tiếng Việt tự nhiên, ngắn gọn.";
                            return new com.example.service.OllamaClient(BASE_URL, MODEL)
                                    .chat(system, ask, 0.2);
                        }

                        @Override
                        protected void done() {
                            try {
                                String reply = get();
                                // Log để debug nếu cần
                                System.out.println("[BOT REPLY] " + reply);

                                // Tạo message bên trái
                                ModelReceiveMessage botMsg = new ModelReceiveMessage();
                                // Nếu ModelReceiveMessage của bạn có các setter khác, vui lòng bổ sung cho đủ
                                botMsg.setFromUserID(chatTitle.getUser().getUserID()); // hiển thị với user đang chat
                                botMsg.setText(reply);
                                try {
                                    botMsg.setMessageType(MessageType.TEXT);
                                } catch (Throwable ignore) {
                                    // nếu project không có enum này thì bỏ qua
                                }

                                chatBody.addItemLeft(botMsg);
                                chatBody.revalidate();
                                chatBody.repaint();
                            } catch (Exception ex) {
                                // Hiển thị lỗi bên trái để bạn thấy ngay
                                ModelReceiveMessage err = new ModelReceiveMessage();
                                err.setFromUserID(chatTitle.getUser().getUserID());
                                err.setText("[BOT ERROR] " + ex.getMessage());
                                try { err.setMessageType(MessageType.TEXT); } catch (Throwable ignore) {}
                                chatBody.addItemLeft(err);
                            }
                        }
                    }.execute();
                }
            }

            @Override
            public void receiveMessage(ModelReceiveMessage data) {
                // Tin từ user khác → hiển thị bên trái nếu đúng user đang mở
                if (chatTitle.getUser().getUserID() == data.getFromUserID()) {
                    chatBody.addItemLeft(data);
                }
            }
        });

        add(chatTitle, "wrap");
        add(chatBody, "wrap");
        add(chatBottom, "h ::50%");
    }

    public void setUser(ModelUserAccount user) {
        chatTitle.setUserName(user);
        chatBottom.setUser(user);
        chatBody.clearChat();
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
