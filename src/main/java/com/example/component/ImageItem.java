package com.example.component;

import com.example.event.EventFileReceiver;
import com.example.event.EventFileSender;
import com.example.model.ModelFileSender;
import com.example.model.ModelReceiveImage;
import com.example.service.Service;
import com.example.swing.blurHash.BlurHash;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageItem extends javax.swing.JLayeredPane {

    public ImageItem() {
        initComponents();
    }
    
    public void setImage(Icon image, ModelFileSender fileSender) {
        fileSender.addEventFileSender(new EventFileSender() {
            @Override
            public void onSending(double percentage) {
                progress.setValue((int) percentage);
            }

            @Override
            public void onStartSending() {
                
            }

            @Override
            public void onFinish() {
                progress.setVisible(false);
            }
        });
        pictureBox.setImage(image);
    }
    
    public void setImage(ModelReceiveImage dataImage) {
        System.out.println("[ImageItem] Setting image with FileID: " + dataImage.getFileID() + ", Width: " + dataImage.getWidth() + ", Height: " + dataImage.getHeight());
        int width = dataImage.getWidth();
        int height = dataImage.getHeight();
        int[] data = BlurHash.decode(dataImage.getImage(), width, height, 1);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, width, height, data, 0, width);
        Icon icon = new ImageIcon(img);
        pictureBox.setImage(icon);
        System.out.println("[ImageItem] BlurHash displayed, starting file download for FileID: " + dataImage.getFileID());
        try {
            Service.getInstance().addFileReceiver(dataImage.getFileID(), new EventFileReceiver() {
                @Override
                public void onReceiving(double percentage) {
                    progress.setValue((int) percentage);
                    if (percentage % 25 == 0) { // Log every 25%
                        System.out.println("[ImageItem] Download progress: " + (int)percentage + "%");
                    }
                }

                @Override
                public void onStartReceiving() {
                    System.out.println("[ImageItem] Started receiving file for FileID: " + dataImage.getFileID());
                }

                @Override
                public void onFinish(File file) {
                    try {
                        System.out.println("[ImageItem] File download finished: " + file.getAbsolutePath());
                        progress.setVisible(false);
                        
                        // Verify file exists and has content
                        if (!file.exists()) {
                            System.err.println("[ImageItem] ERROR: File does not exist: " + file.getAbsolutePath());
                            return;
                        }
                        if (file.length() == 0) {
                            System.err.println("[ImageItem] ERROR: File is empty: " + file.getAbsolutePath());
                            return;
                        }
                        
                        System.out.println("[ImageItem] File verified, size: " + file.length() + " bytes, loading image...");
                        
                        // Add small delay to ensure file is fully written
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        
                        // Read the image from file and create ImageIcon
                        BufferedImage loadedImage = javax.imageio.ImageIO.read(file);
                        if (loadedImage != null) {
                            System.out.println("[ImageItem] Image loaded successfully: " + loadedImage.getWidth() + "x" + loadedImage.getHeight());
                            ImageIcon imageIcon = new ImageIcon(loadedImage);
                            pictureBox.setImage(imageIcon);
                            pictureBox.repaint();
                            pictureBox.revalidate();
                            System.out.println("[ImageItem] Image displayed successfully!");
                        } else {
                            System.err.println("[ImageItem] ERROR: Failed to load image from: " + file.getAbsolutePath());
                            System.err.println("[ImageItem] This usually means the file is corrupted or not a valid image format");
                            
                            // Try to get more info about the file
                            try {
                                java.io.FileInputStream fis = new java.io.FileInputStream(file);
                                byte[] header = new byte[10];
                                int read = fis.read(header);
                                fis.close();
                                System.err.println("[ImageItem] File header (first " + read + " bytes): " + java.util.Arrays.toString(header));
                            } catch (Exception ex) {
                                System.err.println("[ImageItem] Could not read file header: " + ex.getMessage());
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.err.println("[ImageItem] ERROR: Exception loading image file: " + file.getAbsolutePath());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[ImageItem] ERROR: Exception adding file receiver");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pictureBox = new com.example.swing.PictureBox();
        progress = new com.example.swing.Progress();

        progress.setForeground(new java.awt.Color(255, 255, 255));
        progress.setProgressType(com.example.swing.Progress.ProgressType.CANCLE);

        pictureBox.setLayer(progress, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout pictureBoxLayout = new javax.swing.GroupLayout(pictureBox);
        pictureBox.setLayout(pictureBoxLayout);
        pictureBoxLayout.setHorizontalGroup(
            pictureBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pictureBoxLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pictureBoxLayout.setVerticalGroup(
            pictureBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pictureBoxLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        setLayer(pictureBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pictureBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pictureBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.example.swing.PictureBox pictureBox;
    private com.example.swing.Progress progress;
    // End of variables declaration//GEN-END:variables
}
