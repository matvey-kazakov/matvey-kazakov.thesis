// $Id$
/**
 * Date: 02.07.2004
 * Copyright (c) Matvey Kazakov 2003.
 */
package matvey.thesis.visio.bubble;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

/**
 * Класс реализует аплет содержащий визуализатор.
 *
 * @author Matvey Kazakov
 */
public class VisioApplet extends Applet {
    private Visio visio;

    public void init() {
        visio = new Visio();
        setLayout(new BorderLayout());
        // добавляем аплет
        add(visio, BorderLayout.CENTER);
        // устанавливаем Windows Look & Feel
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
        }
    }

    public void start() {
        visio.start();
    }

    public void stop() {
        visio.stop();
    }
}

/*
 * $Log$
 */