/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * 
 * @author lorraineb, seany
 */
public class CardsPanel extends JPanel implements Scrollable {

    private static final int DEFAULT_HGAP = 16;
    private static final int DEFAULT_VGAP = 16;

    public CardsPanel() {
        this(DEFAULT_HGAP, DEFAULT_VGAP);
    }

    public CardsPanel(int hgap, int vgap) {
        setLayout(new WrapLayout(hgap, vgap));
    }

    // Scrollable 

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @Override
    public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
        return 100;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    // WrapLayout
    private class WrapLayout implements LayoutManager {

        private final int hgap;
        private final int vgap;

        public WrapLayout(int hgap, int vgap) {
            this.hgap = hgap;
            this.vgap = vgap;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {}

        @Override
        public void removeLayoutComponent(Component comp) {}

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return calculateSize(parent);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return calculateSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int maxWidth  = parent.getWidth() - (insets.left + insets.right);
                int x = insets.left + hgap;
                int y = insets.top  + vgap;
                int rowHeight = 0;

                for (int i = 0; i < parent.getComponentCount(); i++) {
                    Component c = parent.getComponent(i);
                    if (!c.isVisible()) continue;

                    Dimension d = c.getPreferredSize();

                    if (x + d.width > maxWidth + hgap && x > insets.left + hgap) {
                        x = insets.left + hgap;
                        y += rowHeight + vgap;
                        rowHeight = 0;
                    }

                    c.setBounds(x, y, d.width, d.height);
                    x += d.width + hgap;
                    rowHeight = Math.max(rowHeight, d.height);
                }
            }
        }

        private Dimension calculateSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int maxWidth  = parent.getWidth() - (insets.left + insets.right);

                if (maxWidth <= 0) {
                    // Not yet sized — stack vertically as fallback
                    int totalH = insets.top + vgap;
                    for (int i = 0; i < parent.getComponentCount(); i++) {
                        Component c = parent.getComponent(i);
                        if (!c.isVisible()) continue;
                        Dimension d = c.getPreferredSize();
                        totalH += d.height + vgap;
                    }
                    return new Dimension(200, totalH + insets.bottom);
                }

                int x         = insets.left + hgap;
                int y         = insets.top  + vgap;
                int rowHeight = 0;

                for (int i = 0; i < parent.getComponentCount(); i++) {
                    Component c = parent.getComponent(i);
                    if (!c.isVisible()) continue;

                    Dimension d = c.getPreferredSize();

                    if (x + d.width > maxWidth + hgap && x > insets.left + hgap) {
                        x = insets.left + hgap;
                        y += rowHeight + vgap;
                        rowHeight = 0;
                    }

                    x += d.width + hgap;
                    rowHeight = Math.max(rowHeight, d.height);
                }

                return new Dimension(maxWidth, y + rowHeight + insets.bottom + vgap);
            }
        }
    }
}