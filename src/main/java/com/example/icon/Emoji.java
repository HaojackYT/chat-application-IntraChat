package com.example.icon;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Emoji {
    
    private static Emoji instance;
    
    private Emoji() { }
    
    public static Emoji getInstance() {
        if (instance == null) {
            instance = new Emoji();
        }
        return instance;
    }
    
    public List<ModelEmoji> getStyle1() {
        List<ModelEmoji> emojis = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            emojis.add(new ModelEmoji(i, new ImageIcon(getClass().getResource("/emoji/" + i + ".png"))));
        }
        return emojis;
    }
    
    public List<ModelEmoji> getStyle2() {
        List<ModelEmoji> emojis = new ArrayList<>();
        for (int i = 21; i <= 40; i++) {
            emojis.add(new ModelEmoji(i, new ImageIcon(getClass().getResource("/emoji/" + i + ".png"))));
        }
        return emojis;
    }
    
    public ModelEmoji getEmoji(int id) {
        return new ModelEmoji(id, new ImageIcon(getClass().getResource("/emoji/" + id + ".png")));
    }
}
