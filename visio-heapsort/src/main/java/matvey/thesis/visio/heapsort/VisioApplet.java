// $Id: VisioApplet.java,v 1.1 2006/02/18 06:37:46 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

/**
 * Implements simple applet with visualizer
 * @author Matvey Kazakov
 */
public class VisioApplet extends Applet {

    /**
     * Override method to initialize applet with visualizer's panel.
     */
    public void init() {
        VisioA visio = new VisioA();
        setLayout(new BorderLayout());
        // adding applet
        add(visio, BorderLayout.CENTER);
        // using Windows Look & Feel
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
        }
    }
}

/*
 * $Log: VisioApplet.java,v $
 * Revision 1.1  2006/02/18 06:37:46  matvey
 * HeapSort is committed
 *
 */