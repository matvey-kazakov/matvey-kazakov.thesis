/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.mealy;

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

    private static final Font plainFont = new Font("Arial", Font.PLAIN, 11);
    private static final Font headerFont = plainFont.deriveFont(Font.BOLD);

    // ============== Текстовые сообщения ============================
    private static final String MESSAGE_STATE_0 = "Состояние 0: Начальное состояние";
    private static final String MESSAGE_STATE_1 = "Состояние 1: Вес {0} не может быть получен из нуля предметов";
    private static final String MESSAGE_STATE_2 = "Состояние 2: Вес 0 может быть получен из первых {0} предметов";
    private static final String MESSAGE_STATE_4 = "Состояние 4 (нижняя петля): Заполнение позиции с номером {0}, {1}: перенос значения";
    private static final String MESSAGE_STATE_4_ALT = "Состояние 4 (верхняя петля): Заполнение позиции с номером {0}, {1} : альтернатива";
    private static final String MESSAGE_STATE_5_OK = "Состояние 5: Искомый вес может быть получен";
    private static final String MESSAGE_STATE_5_FAIL = "Состояние 5: Искомый вес не может быть получен";
    private static final String MESSAGE_STATE_7_ALT = "Состояние 7: Выбор обратного хода: альтернатива";
    private static final String MESSAGE_STATE_7 = "Состояние 7: Выбор обратного хода";
    private static final String MESSAGE_STATE_8 = "Состояние 8: Следующая ячейка найдена";
    private static final String MESSAGE_STATE_9 = "Состояние 9: Конец. Искомый абор не существует";
    private static final String MESSAGE_STATE_10 = "Состояние 10: Конец. Искомый набор найден: {0}";

    private Globals globals;

    private JLabel messageLabel;
    private JLabel[][] T;
    private JLabel[] M;

    //  Служебные переменные. Хранят ссылки на предыдущие подсвеченные ячейки
    private JLabel previous = null;
    private JLabel prev_sel1 = null;
    private JLabel prev_sel2 = null;

    public Drawer(Globals globals) {
        this.globals = globals;
        setLayout(new BorderLayout());
        JPanel picturePanel = new JPanel();
        JPanel messagePanel = new JPanel(new BorderLayout());
        add(picturePanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);
        messageLabel = new JLabel();
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        // Иллюстрация представляет из себя массив меток.
        // Для подсветки ячеек используется цвет фона.
        T = new JLabel[globals.T.length][globals.T.length > 0 ? globals.T[0].length : 0];
        M = new JLabel[globals.K];
        picturePanel.setLayout(new GridLayout(globals.K + 2, globals.N + 4));
        picturePanel.setBackground(COLOR_BACKGROUND);
        picturePanel.setBorder(BorderFactory.createLineBorder(Color.white, 10));
        for (int i = 0; i < globals.K + 2; i++) {
            if (i > 0) {
                JLabel[] row = T[i - 1];
                for (int j = 0; j < globals.N + 4; j++) {
                    if (j > 2) {
                        JLabel cell = new JLabel();
                        row[j - 3] = cell;
                        picturePanel.add(createCell(cell));
                    } else if (j == 2) {
                        picturePanel.add(createHeaderCell(i - 1));
                    } else if (j == 0 && i > 1) {
                        M[i - 2] = new JLabel("" + globals.M[i - 2]);
                        picturePanel.add(createCell(M[i - 2]));
                    } else {
                        picturePanel.add(createSpacer());
                    }
                }
            } else {
                for (int j = 0; j < globals.N + 4; j++) {
                    if (j < 3) {
                        picturePanel.add(createSpacer());
                    } else {
                        picturePanel.add(createHeaderCell(j - 3));
                    }
                }
            }
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
     * Формирует пустую панель
     */
    private JPanel createSpacer() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Осуществляет перерисовку
     */
    public void redraw() {
        // Перерисовываем общую картину
        for (int i = 0; i < globals.K + 1; i++) {
            for (int j = 0; j < globals.N + 1; j++) {
                T[i][j].setText(globals.T[i][j] != -1 ? "" + globals.T[i][j] : "");
            }
        }
        for (int i = 0; i < globals.K; i++) {
            if (globals.positions.contains(new Integer(i))) {
                selectCell(M[i], COLOR_CURRENT);
            } else {
                deselectCell(M[i]);
            }
        }
        // Делаем предыдущие подсвеченные ячейки обычными
        deselectCell(previous);
        deselectCell(prev_sel1);
        deselectCell(prev_sel2);
        String newMessage = null;
        // В зависимости от состояния автомата формируем подсвеченные ячейки и сообщение
        switch (globals.state) {
            case 0:
                newMessage = MESSAGE_STATE_0;
                break;
            case 1:
                newMessage = MessageFormat.format(MESSAGE_STATE_1, new Object[]{new Integer(globals.j)});
                selectCell(previous = T[globals.i][globals.j], COLOR_CURRENT);
                break;
            case 2:
                newMessage = MessageFormat.format(MESSAGE_STATE_2, new Object[]{new Integer(globals.i)});
                selectCell(previous = T[globals.i][globals.j], COLOR_CURRENT);
                break;
            case 4:
                if (globals.j >= globals.M[globals.i - 1]) {
                    newMessage = MessageFormat.format(MESSAGE_STATE_4_ALT, new Object[]{
                        new Integer(globals.i), new Integer(globals.j)
                    });
                } else {
                    newMessage = MessageFormat.format(MESSAGE_STATE_4, new Object[]{
                        new Integer(globals.i), new Integer(globals.j)
                    });
                }
                selectCell(previous = T[globals.i][globals.j], COLOR_CURRENT);
                selectCell(prev_sel1 = T[globals.i - 1][globals.j], COLOR_SELECT);
                if (globals.j > globals.M[globals.i - 1]) {
                    selectCell(prev_sel2 = T[globals.i - 1][globals.j - globals.M[globals.i - 1]], COLOR_SELECT);
                }
                break;
            case 5:
                if (globals.T[globals.K][globals.N] == 1) {
                    newMessage = MESSAGE_STATE_5_OK;
                } else {
                    newMessage = MESSAGE_STATE_5_FAIL;
                }
                selectCell(previous = T[globals.i - 1][globals.sum], COLOR_CURRENT);
                break;
            case 7:
                if (globals.j >= globals.M[globals.i - 1]) {
                    newMessage = MESSAGE_STATE_7_ALT;
                } else {
                    newMessage = MESSAGE_STATE_7;
                }
                selectCell(previous = T[globals.i][globals.sum], COLOR_CURRENT);
                selectCell(prev_sel1 = T[globals.i - 1][globals.sum], COLOR_SELECT);
                if (globals.sum >= globals.M[globals.i - 1]) {
                    selectCell(prev_sel2 = T[globals.i - 1][globals.sum - globals.M[globals.i - 1]], COLOR_SELECT);
                }
                break;
            case 8:
                newMessage = MESSAGE_STATE_8;
                selectCell(previous = T[globals.i - 1][globals.sum], COLOR_CURRENT);
                break;
            case 9:
                newMessage = MESSAGE_STATE_9;
                break;
            case 10:
                String itemsString = "[";
                boolean first = true;
                for (Integer pos: globals.positions) {
                    if (first) {
                        first = false;
                    } else {
                        itemsString += ", ";
                    }
                    itemsString += globals.M[pos.intValue()];
                }
                itemsString += "]";
                newMessage = MessageFormat.format(MESSAGE_STATE_10 , new Object[]{itemsString});
                break;
        }
        messageLabel.setText(newMessage);
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