/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.bubble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;

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
    private static final String LABEL_RANDOMIZE = "Генерация";

    // Кнопки
    private JButton buttonStep = new JButton(LABEL_STEP);
    private JButton buttonRestart = new JButton(LABEL_RESTART);
    private JButton buttonAuto = new JButton(LABEL_AUTO);
    private JButton buttonDelayDown = new JButton(LABEL_DELAYDOWN);
    private JButton buttonDelayUp = new JButton(LABEL_DELAYUP);
    private JButton buttonRandomize = new JButton(LABEL_RANDOMIZE);
    private JLabel labelDelay = new JLabel();

    // Паузы
    private static final int INITIAL_DELAY = 4;
    private static final int[] DELAYS = new int[]{10, 20, 50, 100, 250, 500, 1000};

    // Глобальные переменные
    private Globals globals = new Globals(DEFAULT_ARRAY);
    private int[] currentArray;
    // Автомат
    private Automaton automaton = new Automaton(globals);
    // Формирователь иллюстраций и комментариев
    private Drawer drawer = new Drawer(globals);

    private int delay = INITIAL_DELAY;
    private boolean auto = false;

    // Нить, поддерживающая автоматическе выполнение шагов
    private Thread autoThread = new AutoThread();

    private static final int[] DEFAULT_ARRAY = new int[]{5, 6, 11, 8, 10, 14, 9, 7, 12, 2};
    private static final int ARRAY_LENGTH = DEFAULT_ARRAY.length;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Пузырьковая сортировка");
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
        currentArray = DEFAULT_ARRAY;
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
        buttonRandomize.addActionListener(this);

        buttonPanel.add(buttonStep);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(buttonRestart);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(buttonRandomize);
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
            automaton.makeStep();
            updateButtonStep();
            updateAutoButton();
            drawer.redraw();
        } else if (source == buttonRandomize) {
            java.util.List<Integer> values = new ArrayList<Integer>();
            for (int i = 0; i < 40; i++) {
                values.add(i);
            }
            int[] array = new int[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                int value = values.get((int) Math.round(Math.random() * (values.size() - 1)));
                array[i] = value;
                values.remove(Integer.valueOf(value));
            }
            currentArray = array;
            actionPerformed(new ActionEvent(buttonRestart, 0, ""));
        } else if (source == buttonRestart) {
            globals.init(currentArray);
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
        buttonStep.setEnabled(!automaton.isFinished());
    }

    private void updateDelayButtonsState() {
        buttonDelayDown.setEnabled(delay > 0);
        buttonDelayUp.setEnabled(delay < DELAYS.length);
    }

    private void updateLabelDelay() {
        labelDelay.setText(MessageFormat.format(LABEL_DELAY, new Object[]{"" + DELAYS[delay]}));
    }

    private void updateAutoButton() {
        if (automaton.isFinished()) {
            auto = false;
        }
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
 * $Log$
 */