/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.views;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Lorraine G. Bulaclac, seany
 */
public class AuthPanel extends JPanel {
    private static final int DURATION_MS  = 320;  // total animation time
    private static final int FPS          = 60;
    private static final int TIMER_DELAY  = 1000 / FPS;
    private static final int TOTAL_STEPS  = DURATION_MS / TIMER_DELAY;

    private JPanel currentPanel;
    private JPanel nextPanel;

    // offsetX: how far nextPanel is offset from its final position
    // Ranges from +width (offscreen right) → 0 (in place)
    // or from -width (offscreen left) → 0
    private int offsetX      = 0;
    private int step         = 0;
    private boolean sliding  = false;
    private Timer timer;

    public AuthPanel() {
        setLayout(null); // manual positioning during animation
        setOpaque(false);

        currentPanel = new LoginPanel();
        add(currentPanel);
    }

    // Public API

    /** Slide to RegisterPanel (slides left — register comes from right) */
    public void showRegister() {
        if (sliding) return;
        slideToPanel(new RegisterPanel(), true);
    }

    /** Slide to LoginPanel (slides right — login comes from left) */
    public void showLogin() {
        if (sliding) return;
        slideToPanel(new LoginPanel(), false);
    }

    // Core animation

    private void slideToPanel(JPanel target, boolean toRight) {
        nextPanel = target;
        add(nextPanel);

        int w = getWidth();

        // Start: nextPanel is offscreen
        // toRight = true  → going to register → next starts at +w (comes from right)
        // toRight = false → going to login    → next starts at -w (comes from left)
        final int startOffset  = toRight ? w : -w;
        final int currentStart = 0;
        final int currentEnd   = toRight ? -w : w;

        offsetX  = startOffset;
        step     = 0;
        sliding  = true;

        positionPanels(currentStart, startOffset);

        if (timer != null && timer.isRunning()) timer.stop();

        timer = new Timer(TIMER_DELAY, e -> {
            step++;
            float progress = easeInOut((float) step / TOTAL_STEPS);

            int curX  = (int)(currentStart + (currentEnd  - currentStart)  * progress);
            int nextX = (int)(startOffset  + (0           - startOffset)   * progress);

            positionPanels(curX, nextX);
            repaint();

            if (step >= TOTAL_STEPS) {
                ((Timer) e.getSource()).stop();
                sliding = false;

                // Swap panels
                remove(currentPanel);
                currentPanel = nextPanel;
                nextPanel    = null;

                positionPanels(0, getWidth()); // snap current into place
                revalidate();
                repaint();
            }
        });

        timer.start();
    }

    private void positionPanels(int currentX, int nextX) {
        int w = getWidth();
        int h = getHeight();

        if (currentPanel != null) {
            currentPanel.setBounds(currentX, 0, w, h);
        }
        if (nextPanel != null) {
            nextPanel.setBounds(nextX, 0, w, h);
        }
    }

    // Ease function

    /** Smooth ease-in-out (cubic) */
    private float easeInOut(float t) {
        t = Math.min(1f, Math.max(0f, t));
        return t < 0.5f
            ? 4 * t * t * t
            : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
    }

    // Layout

    @Override
    public void doLayout() {
        super.doLayout();
        if (!sliding && currentPanel != null) {
            currentPanel.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
