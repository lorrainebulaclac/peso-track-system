/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.Timer;
/**
 *
 * @author Lorraine G. Bulaclac, seany
 */
public class SpinnerButton extends JButton{
    
    private static final int   SPINNER_SIZE  = 16;
    private static final int   SPINNER_THICK = 2;
    private static final int   ARC_ANGLE     = 90;
    private static final int   TIMER_DELAY   = 16; // ~60fps
    private static final int   ROTATION_STEP = 8;  // degrees per frame

    private boolean loading   = false;
    private int     angle     = 0;
    private Timer   timer;
    private String  normalText;
    private Color   spinnerColor = Color.WHITE;

    public SpinnerButton() {
        super();
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public SpinnerButton(String text) {
        this();
        setText(text);
        this.normalText = text;
    }

    // Public API

    public void startLoading() {
        if (loading) return;
        normalText = getText();
        loading    = true;
        setEnabled(false);
        setText("");  // hide text while spinning

        timer = new Timer(TIMER_DELAY, e -> {
            angle = (angle + ROTATION_STEP) % 360;
            repaint();
        });
        timer.start();
    }

    public void stopLoading() {
        if (!loading) return;
        loading = false;
        if (timer != null) timer.stop();
        setEnabled(true);
        setText(normalText);
        repaint();
    }

    public void setSpinnerColor(Color color) {
        this.spinnerColor = color;
    }

    public boolean isLoading() {
        return loading;
    }

    // Paint 

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!loading) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int cx = getWidth()  / 2;
        int cy = getHeight() / 2;
        int r  = SPINNER_SIZE / 2;

        // Track (faint background arc)
        g2.setStroke(new BasicStroke(SPINNER_THICK, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(spinnerColor.getRed(), spinnerColor.getGreen(),
                spinnerColor.getBlue(), 50));
        g2.drawOval(cx - r, cy - r, SPINNER_SIZE, SPINNER_SIZE);

        // Spinning arc
        g2.setColor(spinnerColor);
        g2.drawArc(cx - r, cy - r, SPINNER_SIZE, SPINNER_SIZE, -angle, ARC_ANGLE);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        // Ensure minimum height so spinner has room
        d.height = Math.max(d.height, 36);
        return d;
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (timer != null) timer.stop();
    }
}
