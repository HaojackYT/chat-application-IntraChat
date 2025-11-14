package com.example.model;

import com.example.event.EventFileSender;
import com.example.service.Service;
import io.socket.client.Ack;
import io.socket.client.Socket;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ModelFileSender {

    private int fileID;
    private RandomAccessFile accessFile;
    private File file;
    private Socket socket;
    private ModelSendMessage message;
    private String fileExtensions;
    private long fileSize;
    private EventFileSender eventFileSender;

    public ModelFileSender(File file, Socket socket, ModelSendMessage message) throws IOException {
        accessFile = new RandomAccessFile(file, "r");
        this.file = file;
        this.socket = socket;
        this.message = message;
        fileExtensions = getExtensions(file.getName());
        fileSize = accessFile.length();
    }

    public ModelFileSender() {
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    public synchronized byte[] readFile() throws IOException {
        long filepointer = accessFile.getFilePointer();
        if (filepointer != fileSize) {
            int max = 2000;
            long length;
            if (filepointer + max >= fileSize) {
                length = fileSize - filepointer;
            } else {
                length = max;
            }
            byte[] data = new byte[(int) length];
            accessFile.read(data);
            return data;
        } else {
            return null;
        }
    }

    public void initSend() throws IOException {
        socket.emit("send_to_user", message.toJSONObject(), new Ack() {
            @Override
            public void call(Object... args) {
                if (args.length > 0) {
                    int fileID = (int) args[0];
                    try {
                        startSend(fileID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void startSend(int fileID) throws IOException {
        this.fileID = fileID;
        if (eventFileSender != null) {
            eventFileSender.onStartSending();
        }
        sendingFile();
    }

    private void sendingFile() throws IOException {
        ModelPackageSender data = new ModelPackageSender();
        data.setFileID(fileID);
        byte[] bytes = readFile();
        if (bytes != null) {
            // Send binary chunk as byte[] so socket.io can transport as binary
            data.setData(bytes);
            data.setFinish(false);
        } else {
            data.setFinish(true);
            close();
        }
        socket.emit("send_file", data.toJSONPObject(), new Ack() {
            @Override
            public void call(Object... args) {
                if (args.length > 0) {
                    boolean act = (boolean) args[0];
                    if (act) {
                        try {
                            if (!data.isFinish()) {
                                if (eventFileSender != null) {
                                    eventFileSender.onSending(getPercentage());
                                }
                                sendingFile();
                            } else {
                                // File send finish
                                Service.getInstance().fileSendFinish(ModelFileSender.this);
                                if (eventFileSender != null) {
                                    eventFileSender.onFinish();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public double getPercentage() throws IOException {
        double percentage;
        long filePointer = accessFile.getFilePointer();
        percentage = filePointer * 100 / fileSize;
        return percentage;
    }

    public void close() throws IOException {
        accessFile.close();
    }

    public void addEventFileSender(EventFileSender eventFileSender) {
        this.eventFileSender = eventFileSender;
    }

    public ModelSendMessage getMessage() {
        return message;
    }

    public void setMessage(ModelSendMessage message) {
        this.message = message;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getFileExtensions() {
        return fileExtensions;
    }

    public void setFileExtensions(String fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public RandomAccessFile getAccFile() {
        return accessFile;
    }

    public void setAccFile(RandomAccessFile accFile) {
        this.accessFile = accFile;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
