package com.example.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

public class PanelSlide extends javax.swing.JPanel {

    public int getAnimate() {
        return animate;
    }

    public void setAnimate(int animate) {
        this.animate = animate;
    }

    public PanelSlide() {
        list = new ArrayList<>();
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                animate();
            }
        });
        
        // Ensure child components always match this panel's size so content is centered
        // and margins remain symmetric when the container grows/shrinks.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                for (Component c : list) {
                    c.setSize(size);
                    // Keep the component anchored at (0,0) so sliding math stays correct
                    // and there is no extra space added on either side.
                    c.setLocation(0, 0);
                }
            }
        });
        
    }

    private final List<Component> list;
    private final Timer timer;
    private Component comExit;
    private Component comShow;
    private int currentShowing;
    private boolean animateRight;
    private int animate = 1;

    public void init(Component... com) {
        if (com.length > 0) {
            for (Component c : com) {
                list.add(c);
//                c.setSize(getPreferredSize());

                // Use current size if available; fall back to preferred size during first layout
                Dimension size = getSize();
                if (size.width <= 0 || size.height <= 0) {
                    size = getPreferredSize();
                }
                c.setSize(size);
                
                c.setVisible(false);
                this.add(c);
            }
            //  get first componect to show on panel when init
            Component show = list.get(0);
            show.setVisible(true);
            show.setLocation(0, 0);
        }
    }

    public void show(int index) {
        if (!timer.isRunning()) {
            if (list.size() > 1 && index < list.size() && index != currentShowing) {
                comShow = list.get(index);
                comExit = list.get(currentShowing);
                animateRight = index < currentShowing;
                currentShowing = index;
                comShow.setVisible(true);
                if (animateRight) {
                    comShow.setLocation(-comShow.getWidth(), 0);
                } else {
                    comShow.setLocation(getWidth(), 0);
                }
                timer.start();
            }
        }
    }

    private void animate() {
        if (animateRight) {
            if (comShow.getLocation().x < 0) {
                comShow.setLocation(comShow.getLocation().x + animate, 0);
                comExit.setLocation(comExit.getLocation().x + animate, 0);
            } else {
                //  Stop animate
                comShow.setLocation(0, 0);
                timer.stop();
                comExit.setVisible(false);
            }
        } else {
            if (comShow.getLocation().x > 0) {
                comShow.setLocation(comShow.getLocation().x - animate, 0);
                comExit.setLocation(comExit.getLocation().x - animate, 0);
            } else {
                comShow.setLocation(0, 0);
                timer.stop();
                comExit.setVisible(false);
            }
        }
    }
}
