/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id: Visio.java,v 1.1 2005/01/04 08:42:55 matvey Exp $
 */
package matvey.thesis.visio.prime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

/**
 * Главный класс визуализатора
 *
 * @author Matvey Kazakov
 */
public class Visio extends JPanel implements ActionListener {

    // Надписи на кнопках
    private static final String LABEL_AUTO = "Авто";
    private static final String LABEL_STOP = "Стоп";
    private static final String LABEL_STEP = ">>";
    private static final String LABEL_RESTART = "Заново";
    private static final String LABEL_DELAYUP = ">";
    private static final String LABEL_DELAYDOWN = "<";
    private static final String LABEL_DELAY = "Пауза: {0}";

    // Кнопки
    private JButton buttonStep = new JButton(LABEL_STEP);
    private JButton buttonRestart = new JButton(LABEL_RESTART);
    private JButton buttonAuto = new JButton(LABEL_AUTO);
    private JButton buttonDelayDown = new JButton(LABEL_DELAYDOWN);
    private JButton buttonDelayUp = new JButton(LABEL_DELAYUP);
    private JLabel labelDelay = new JLabel();

    // Паузы
    private static final int INITIAL_DELAY = 4;
    private static final int[] DELAYS = new int[]{10, 20, 50, 100, 250, 500, 1000};

    // Глобальные переменные
    private Globals globals = new Globals(100);
    // Формирователь иллюстраций и комментариев
    private Drawer drawer = new Drawer(globals);

    private int delay = INITIAL_DELAY;
    private boolean auto = false;

    // Нить, поддерживающая автоматическе выполнение шагов
    private Thread autoThread = new AutoThread();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Решето Эратосфена");
        Visio visio = new Visio();
        visio.start();
        frame.setContentPane(visio);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Формирование панели управления
     */
    public Visio() {
        setBorder(BorderFactory.createLineBorder(getBackground(), 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        add(drawer);
        buttonStep.addActionListener(this);
        buttonRestart.addActionListener(this);
        buttonAuto.addActionListener(this);
        buttonDelayDown.addActionListener(this);
        buttonDelayUp.addActionListener(this);

        buttonPanel.add(buttonStep);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(buttonRestart);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(Box.createHorizontalGlue());

        buttonPanel.add(buttonAuto);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonDelayDown.setMargin(new Insets(0, 3, 0, 3));
        buttonDelayUp.setMargin(new Insets(0, 3, 0, 3));
        buttonPanel.add(buttonDelayDown);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(labelDelay);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(buttonDelayUp);

        updateLabelDelay();

        add(Box.createVerticalStrut(10));
        add(buttonPanel);
        drawer.redraw();
    }

    /**
     * Стартует поток автоматического перехода
     */
    public void start() {
        autoThread.start();
    }

    /**
     * Останавливает поток автоматического перехода
     */
    public void stop() {
        autoThread.interrupt();
    }

    /**
     * Реакция на нажатие кнопок
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == buttonStep) {
            globals.auto.makeStep();
            updateButtonStep();
            drawer.redraw();
        } else if (source == buttonRestart) {
            globals.init();
            auto = false;
            updateAutoButton();
            updateButtonStep();
            drawer.redraw();
        } else if (source == buttonAuto) {
            auto = !auto;
            updateAutoButton();
        } else if (source == buttonDelayDown) {
            if (delay > 0) {
                delay--;
                updateLabelDelay();
                autoThread.interrupt();
            }
            updateDelayButtonsState();
        } else if (source == buttonDelayUp) {
            if (delay < DELAYS.length) {
                delay++;
                updateLabelDelay();
                autoThread.interrupt();
            }
            updateDelayButtonsState();
        }
    }

    // ========== Функции обновления состояния кнопок =====================

    private void updateButtonStep() {
    	buttonStep.setEnabled(!globals.auto.isFinished());
    }

    private void updateDelayButtonsState() {
        buttonDelayDown.setEnabled(delay > 0);
        buttonDelayUp.setEnabled(delay < DELAYS.length);
    }

    private void updateLabelDelay() {
        labelDelay.setText(MessageFormat.format(LABEL_DELAY, new Object[]{"" + DELAYS[delay]}));
    }

    private void updateAutoButton() {
        buttonAuto.setText(auto ? LABEL_STOP : LABEL_AUTO);
    }

    /**
     * Нить, в случае автоматического режима через определенные интервалы инициирующая
     * шаг визуализатора.
     */
    private class AutoThread extends Thread {
        public AutoThread() {
            super("Auto thread");
            setDaemon(true);
        }

        public void run() {
            while (true) {
                if (auto) {
                    actionPerformed(new ActionEvent(buttonStep, 0, ""));
                    if (globals.auto.isFinished()) {
                    	auto = false;
                    	updateAutoButton();
                    }
                }
                try {
                    sleep(DELAYS[delay]);
                } catch (InterruptedException e) {
                }
            }
        }
    }

}

/*
 * $Log: Visio.java,v $
 * Revision 1.1  2005/01/04 08:42:55  matvey
 * Just added
 *
 */