/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.bubble;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * Класс отвечает за формирование сообщений и иллюстраций
 *
 * @author Matvey Kazakov
 */
public class Drawer extends JPanel {

    // =============== Константы для рисования ========================
    private static final Color COLOR_BORDER = Color.black;
    private static final Color COLOR_CURRENT = Color.lightGray;
    private static final Color COLOR_SELECT = Color.gray;
    private static final Color COLOR_BACKGROUND = Color.white;
    private static final Color COLOR_FILLED = Color.green;

    private static final Font plainFont = new Font("Arial", Font.PLAIN, 11);
    private static final Font headerFont = plainFont.deriveFont(Font.BOLD);

    // ============== Текстовые сообщения ============================
    private static final String MESSAGE_STATE_START = "Начальное состояние";
    private static final String MESSAGE_STATE_FINISH = "Конечное состояние";
    private static final String MESSAGE_STATE_NEXT = "Элемент с номером {0} найден";
    private static final String MESSAGE_STATE_EMERGE = "Сравниваем элементы с номерами {0} и {1}";
    private static final String MESSAGE_STATE_SWAP = "Обмениваем значения элементов с номерами {0} и {1}";

    private Globals g;

    private JLabel messageLabel;
    private JLabel[] a;

    //  Служебные переменные. Хранят ссылки на предыдущие подсвеченные ячейки
    private JLabel prev_sel1 = null;
    private JLabel prev_sel2 = null;

    public Drawer(Globals globals) {
        this.g = globals;
        setLayout(new BorderLayout());
        JPanel picturePanel = new JPanel();
        JPanel messagePanel = new JPanel(new BorderLayout());
        add(picturePanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);
        messageLabel = new JLabel();
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        // Иллюстрация представляет из себя массив меток.
        // Для подсветки ячеек используется цвет фона.
        a = new JLabel[globals.n];
        picturePanel.setLayout(new GridLayout(2, globals.n));
        picturePanel.setBackground(COLOR_BACKGROUND);
        picturePanel.setBorder(BorderFactory.createLineBorder(Color.white, 10));
        for (int i = 0; i < globals.n; i++) {
            picturePanel.add(createHeaderCell(i));
        }
        for (int i = 0; i < globals.n; i++) {
            JLabel cell = new JLabel("" + globals.a[i]);
            a[i] = cell;
            picturePanel.add(createCell(cell));
        }
    }

    /**
     * Формирует панель с черной границей
     */
    private JPanel createCell(JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        panel.add(label, BorderLayout.CENTER);
        panel.setOpaque(false);
        label.setOpaque(false);
        label.setFont(plainFont);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return panel;
    }

    /**
     * Формирует ячейку в заголовке.
     */
    private JPanel createHeaderCell(int i) {
        JLabel label = new JLabel("" + i);
        JPanel panel = createCell(label);
        label.setFont(headerFont);
        return panel;
    }

    /**
     * Осуществляет перерисовку
     */
    public void redraw() {
        for (int i = 0; i < g.n; i++) {
            a[i].setText("" + g.a[i]);
        }
        // Делаем предыдущие подсвеченные ячейки обычными
        deselectCell(prev_sel1);
        deselectCell(prev_sel2);
        String newMessage = null;
        // В зависимости от состояния автомата формируем подсвеченные ячейки и сообщение
        switch (g.state) {
            case Automaton.STATE_START:
                newMessage = MESSAGE_STATE_START;
                for (int i = 0; i < a.length; i++) {
                    deselectCell(a[i]);
                }
                break;
            case Automaton.STATE_INIT2:
                newMessage = MessageFormat.format(MESSAGE_STATE_EMERGE, new Object[]{
                    new Integer(g.j), new Integer(g.j + 1)
                });
                selectCurrentCells();
                break;
            case Automaton.STATE_EMERGE:
                if (g.j < g.n - g.i + 1) {
                    newMessage = MessageFormat.format(MESSAGE_STATE_EMERGE, new Object[]{
                        new Integer(g.j), new Integer(g.j + 1)
                    });
                    selectCurrentCells();
                } else {
                    newMessage = MessageFormat.format(MESSAGE_STATE_NEXT, new Object[]{
                        new Integer(g.n - g.i + 1)
                    });
                    prev_sel1 = prev_sel2 = null;
                    selectCell(a[g.n - g.i], COLOR_FILLED);
                }
                break;
            case Automaton.STATE_SWAP:
                newMessage = MessageFormat.format(MESSAGE_STATE_SWAP, new Object[]{
                    new Integer(g.j), new Integer(g.j + 1)
                });
                selectCurrentCells();
                break;
            case Automaton.STATE_FINISH:
                newMessage = MESSAGE_STATE_FINISH;
                selectCell(a[g.n - g.i], COLOR_FILLED);
                break;
        }
        messageLabel.setText(newMessage);
    }

    private void selectCurrentCells() {
        if (g.a[g.j] > g.a[g.j - 1]) {
            selectCell(prev_sel1 = a[g.j - 1], COLOR_CURRENT);
            selectCell(prev_sel2 = a[g.j], COLOR_SELECT);
        } else {
            selectCell(prev_sel1 = a[g.j - 1], COLOR_SELECT);
            selectCell(prev_sel2 = a[g.j], COLOR_CURRENT);
        }
    }

    /**
     * Выделяем ячейку определенным цветом
     */
    private void selectCell(JLabel cell, Color color) {
        cell.setOpaque(true);
        cell.setBackground(color);
        cell.revalidate();
        cell.repaint();
    }

    private void deselectCell(JLabel cell) {
        if (cell != null) {
            cell.setOpaque(false);
            cell.revalidate();
            cell.repaint();
        }
    }

}

/*
 * $Log$
 */