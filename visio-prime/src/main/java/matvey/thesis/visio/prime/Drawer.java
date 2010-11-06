/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.prime;

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
    private static final Color COLOR_PRIME = Color.green;
    private static final Color COLOR_SELECT_PRIME = Color.green.darker();
    private static final Color COLOR_SELECT = Color.red.darker();
    private static final Color COLOR_BACKGROUND = Color.white;
    private static final Color COLOR_NON_PRIME = Color.lightGray;

    private static final Font plainFont = new Font("Arial", Font.PLAIN, 11);
    private static final Font selectFont = new Font("Arial", Font.BOLD, 11);

    // ============== Текстовые сообщения ============================
    private static final String MESSAGE_STATE_START = "Начальное состояние";
    private static final String MESSAGE_STATE_NEXT = "Cледующее простое число {0}";
    private static final String MESSAGE_STATE_FILL = "Число {0} не является простым";
    private static final String MESSAGE_STATE_ADDON = "{0} > SQRT({1}). Отсев закончен";
    private static final String MESSAGE_STATE_END = "Простые числа найдены";

    private Globals g;

    private JLabel messageLabel;
    private JLabel[] a;

    //  Служебные переменные. Хранят ссылки на предыдущие подсвеченные ячейки
    private int prev_sel = -1;

    public Drawer(Globals globals) {
        this.g = globals;
        setLayout(new GridBagLayout());
        JPanel picturePanel = new JPanel();
        JPanel messagePanel = new JPanel(new BorderLayout());
        setBackground(COLOR_BACKGROUND);
        add(picturePanel, new GridBagConstraints(0, 0, 1, 1, 1d, 1d, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(messagePanel, new GridBagConstraints(0, 1, 1, 1, 1d, 0d, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        messageLabel = new JLabel();
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        // Иллюстрация представляет из себя массив меток.
        // Для подсветки ячеек используется цвет фона.
        a = new JLabel[globals.n+1];
        picturePanel.setLayout(new GridLayout(10, globals.n / 10));
        picturePanel.setBackground(COLOR_BACKGROUND);
        picturePanel.setBorder(BorderFactory.createLineBorder(Color.white, 10));
        for (int i = 1; i <= globals.n; i++) {
            JLabel cell = new JLabel("" + i);
            a[i] = cell;
            if (i == 1) {
                picturePanel.add(createEmptyCell(cell));
            } else {
                picturePanel.add(createCell(cell));
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
    	label.setOpaque(true);
    	label.setFont(plainFont);
    	label.setHorizontalAlignment(SwingConstants.CENTER);
    	label.setPreferredSize(new Dimension(25, 20));
    	return panel;
    }
    /**
     * Формирует панель без границы
     */
    private JPanel createEmptyCell(JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.setOpaque(false);
        label.setOpaque(false);
    	label.setText("");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(25, 20));
        return panel;
    }

    /**
     * Осуществляет перерисовку
     */
    public void redraw() {
        // Делаем предыдущие подсвеченные ячейки обычными
        deselectCell(prev_sel, false);
        String newMessage = null;
        // В зависимости от состояния автомата формируем подсвеченные ячейки и сообщение
        switch (g.state) {
            case Automaton.STATE_START:
                for (int i = 1; i < a.length; i++) {
                    a[i].setFont(plainFont);
                    deselectCell(i, false);
                }
                a[1].setFont(selectFont);
                newMessage = MESSAGE_STATE_START;
                break;
            case Automaton.STATE_NEXT:
            	if (g.i > g.q) {
            		newMessage = MessageFormat.format(MESSAGE_STATE_ADDON, new Object[]{g.i, g.n});
            	} else {
                	newMessage = g.a[g.i] ? MessageFormat.format(MESSAGE_STATE_NEXT, new Object[]{g.i}) :
                		MessageFormat.format(MESSAGE_STATE_FILL, new Object[]{g.i});
            	}
                selectCell(g.i);
                prev_sel = g.i;
                a[g.i].setFont(selectFont);
                break;
            case Automaton.STATE_FILL:
                selectCell(g.j);
                prev_sel = g.j;
                newMessage = MessageFormat.format(MESSAGE_STATE_FILL, new Object[]{g.j});
                break;
            case Automaton.STATE_END:
                newMessage = MessageFormat.format(MESSAGE_STATE_END, new Object[]{});
                for (int i = 1; i < a.length; i++) {
                    deselectCell(i, true);
                }
                break;
        }
        messageLabel.setText(newMessage);
    }

    /**
     * Выделяем ячейку определенным цветом
     */
    private void selectCell(int i) {
    	JLabel cell = a[i];    	
        cell.setBackground(g.a[i] ? COLOR_SELECT_PRIME : COLOR_SELECT);
        cell.revalidate();
        cell.repaint();
    }

    private void deselectCell(int cellIdx, boolean force) {
        if (cellIdx >= 0) {
        	JLabel cell = a[cellIdx];
            if (!g.a[cellIdx]) {
            	cell.setBackground(COLOR_NON_PRIME);
            } else if (force || cellIdx <= g.i){
            	cell.setBackground(COLOR_PRIME);
            } else {
            	cell.setBackground(COLOR_BACKGROUND);
            }
            cell.revalidate();
            cell.repaint();
        }
    }

}

/*
 * $Log$
 */