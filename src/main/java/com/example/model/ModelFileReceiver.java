package com.example.model;

import com.example.event.EventFileReceiver;
import io.socket.client.Ack;
import io.socket.client.Socket;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ModelFileReceiver {

    private int fileID;
    private Socket client;
    private File file;
    private RandomAccessFile accessFile;
    private String fileExtension;
    private long fileSize;
    private EventFileReceiver event;

    public ModelFileReceiver(int fileID, Socket client, EventFileReceiver event) {
        this.fileID = fileID;
        this.client = client;
        this.event = event;
    }

    public void initReceive() throws IOException {
        event.onStartReceiving();
        client.emit("get_file", fileID, new Ack() {
            @Override
            public void call(Object... os) {
                if (os.length > 0) {
                    fileExtension = os[0].toString();
                    fileSize = Long.parseLong(os[1].toString());
                    try {
                        file = new File("client_data/" + fileID + fileExtension);
                        System.out.println("[ModelFileReceiver] Receiving file: " + file.getAbsolutePath() + ", size: " + fileSize);
                        // Create parent directory if not exists
                        File parentDir = file.getParentFile();
                        if (parentDir != null && !parentDir.exists()) {
                            boolean created = parentDir.mkdirs();
                            System.out.println("[ModelFileReceiver] Created directory: " + parentDir.getAbsolutePath() + ", success: " + created);
                        }
                        accessFile = new RandomAccessFile(file, "rw");
                        requestFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void requestFile() throws IOException {
        long currentLength = accessFile.length();
        System.out.println("[ModelFileReceiver] Requesting chunk at position: " + currentLength);
        ModelRequestFile data = new ModelRequestFile(fileID, currentLength);
        client.emit("request_file", data.toJSONObject(), new Ack() {
            @Override
            public void call(Object... os) {
                try {
                    if (os.length > 0) {
                        System.out.println("[ModelFileReceiver] Received response, type: " + os[0].getClass().getName());
                        
                        byte[] b;
                        if (os[0] instanceof byte[]) {
                            b = (byte[]) os[0];
                        } else {
                            System.err.println("[ModelFileReceiver] ERROR: Received data is not byte[], it's: " + os[0].getClass());
                            close();
                            return;
                        }
                        
                        System.out.println("[ModelFileReceiver] Received chunk size: " + b.length + " bytes");
                        
                        // Debug: print first few bytes
                        if (b.length > 0) {
                            StringBuilder sb = new StringBuilder("[ModelFileReceiver] First bytes (raw): ");
                            for (int i = 0; i < Math.min(10, b.length); i++) {
                                sb.append(String.format("%02X ", b[i] & 0xFF));
                            }
                            System.out.println(sb.toString());
                        }
                        
                        // CRITICAL FIX: Socket.IO adds a 0x04 byte at the beginning of binary data
                        if (b.length > 0 && b[0] == 0x04) {
                            System.out.println("[ModelFileReceiver] Stripping Socket.IO binary marker (0x04)");
                            byte[] stripped = new byte[b.length - 1];
                            System.arraycopy(b, 1, stripped, 0, b.length - 1);
                            b = stripped;
                            System.out.println("[ModelFileReceiver] After stripping, size: " + b.length + " bytes");
                            
                            if (b.length > 0) {
                                StringBuilder sb = new StringBuilder("[ModelFileReceiver] First bytes (clean): ");
                                for (int i = 0; i < Math.min(10, b.length); i++) {
                                    sb.append(String.format("%02X ", b[i] & 0xFF));
                                }
                                System.out.println(sb.toString());
                            }
                        }
                        
                        long beforeWrite = accessFile.length();
                        writeFile(b);
                        long afterWrite = accessFile.length();
                        System.out.println("[ModelFileReceiver] File size: " + beforeWrite + " -> " + afterWrite + ", Expected: " + fileSize);
                        event.onReceiving(accessFile.length() * 100 / fileSize);
                        if (accessFile.length() < fileSize) {
                            requestFile();
                        } else {
                            System.out.println("[ModelFileReceiver] Download complete!");
                            close();
                        }
                    } else {
                        // No data returned
                        System.out.println("[ModelFileReceiver] No data returned from server");
                        close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private synchronized long writeFile(byte[] data) throws IOException {
        long currentPos = accessFile.length();
        accessFile.seek(currentPos);
        accessFile.write(data);
        long newLength = accessFile.length();
        System.out.println("[ModelFileReceiver] Wrote " + data.length + " bytes at position " + currentPos + ", new length: " + newLength);
        return newLength;
    }

    public void close() throws IOException {
        // Ensure all data is written to disk before closing
        if (accessFile != null) {
            accessFile.getFD().sync(); // Force sync to disk
            accessFile.close();
        }
        System.out.println("[ModelFileReceiver] File received successfully: " + file.getAbsolutePath() + ", size: " + file.length());
        event.onFinish(file);
        com.example.service.Service.getInstance().fileReceiveFinish(this);
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
