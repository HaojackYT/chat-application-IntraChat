package com.example.icon;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ModelEmoji {
    
    int id;
    Icon icon;
    
    public ModelEmoji(int id, Icon icon) {
        this.id = id;
        this.icon = icon;
    }
    
    public ModelEmoji() { }
    
    public ModelEmoji toSize(int x, int y) {
        return new ModelEmoji(id, new ImageIcon(((ImageIcon) icon).getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH)));
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
    
    
}
