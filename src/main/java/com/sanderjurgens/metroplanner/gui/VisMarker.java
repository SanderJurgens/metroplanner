package com.sanderjurgens.metroplanner.gui;

import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * The VisMarker is a little flag that can be placed onto the JPanel
 *
 * @author sanderjurgens
 */
public class VisMarker extends VisComponent {

    /** Flag image */
    private final ImageIcon image;

    /**
     * Constructs a new flag.
     *
     * @param url uniform resource locator for marker image
     */
    public VisMarker(URL url) {
        this.setLayout(new BorderLayout());
        image = new ImageIcon(url);
        this.add(new JLabel(image));
    }

    /**
     * Place the component such that the base of the flag is on the specified
     * position
     *
     * @param x the horizontal coordinate of the flag base
     * @param y the vertical coordinate of the flag base
     */
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.setBounds(x - image.getIconWidth() / 2, y - image.getIconHeight(), image.getIconWidth(), image.getIconHeight());
    }
}
