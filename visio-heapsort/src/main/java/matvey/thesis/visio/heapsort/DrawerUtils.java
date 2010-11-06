// $Id: DrawerUtils.java,v 1.1 2006/02/18 06:37:44 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Utility class contains methods for drawing miscellaneous parts of vialuser picture {@link matvey.visio.treetraversal.animation.TreeTraversalDrawer}
 *
 * @author Matvey Kazakov
 */
class DrawerUtils {
    // =============== Drawing constants ========================
    protected static final Color COLOR_BORDER = Color.black;
    protected static final Color COLOR_SELECT = Color.lightGray;
    protected static final Color COLOR_CURRENT = Color.gray;
    protected static final Color COLOR_BACKGROUND = Color.white;
    protected static final Font plainFont = new Font("Arial", Font.PLAIN, 11);
    protected static final Font boldFont = plainFont.deriveFont(Font.BOLD);
    protected static final int DISTANCE = 40;
    protected static final int RADIUS = 10;
    protected static final int DIAMETER = RADIUS * 2;

    /**
     * Creates panle with array of labels.
     * @param label text to write inall labels
     * @param axis direction to draw array in
     * @param labelArray array to fill with new labels.
     * @return created panel with labels
     */
    protected static JPanel createArrayPanel(String label, int axis, JLabel[] labelArray) {
        String labelPlacement =  BorderLayout.NORTH;
        JPanel superpanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, axis));
        for (int i = 0; i < labelArray.length; i++) {
            labelArray[i] = new JLabel(" ");
            labelArray[i].setPreferredSize(new Dimension(DIAMETER, DIAMETER));
            panel.add(createCell(labelArray[i]));
        }
        superpanel.setBackground(COLOR_BACKGROUND);
        panel.setBackground(COLOR_BACKGROUND);
        JLabel panelLabel = new JLabel(label);
        superpanel.add(panelLabel, labelPlacement);
        superpanel.add(panel, BorderLayout.CENTER);
        return superpanel;
    }

    /**
     * Creates transparent label with border
     */
    protected static JPanel createCell(JLabel label) {
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
     * Makes label non-selected.
     * @param lastSelectedCell label to decelect
     */
    protected static void deselectCell(JLabel lastSelectedCell) {
        if (lastSelectedCell != null) {
            lastSelectedCell.setOpaque(false);
            lastSelectedCell.revalidate();
            lastSelectedCell.repaint();
            lastSelectedCell = null;
        }
    }

    /**
     * Draws tree edge betweeen given points.
     *
     * @param g graphics to draw on
     * @param from start point
     * @param to end point
     */
    protected static void drawArrow(Graphics g, Point from, Point to) {
        double distance = Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y));
        if (distance <= 0) {
            return;
        }
        double xCoeff = (to.x - from.x) / distance;
        double yCoeff = (to.y - from.y) / distance;
        int x1 = (int)(from.x + RADIUS * xCoeff);
        int y1 = (int)(from.y + RADIUS * yCoeff);
        int x2 = (int)(to.x - RADIUS * xCoeff);
        int y2 = (int)(to.y - RADIUS * yCoeff);
        g.drawLine(x1, y1, x2, y2);
        double alpha = Math.PI / 6; // 30 ��������
        double arrowLength = distance / 5;
        drawArrowPart(g, x2, y2, alpha, xCoeff, yCoeff, arrowLength);
        drawArrowPart(g, x2, y2, -alpha, xCoeff, yCoeff, arrowLength);
    }

    /**
     * Draws piece of arrow on the tree edge.
     * It draws line from starting point with given angle from given direction.
     * @param g graphics to draw on
     * @param x start point x
     * @param y start point y
     * @param alpha angle from base edge line
     * @param xCoeff base edge line slope coefficient x
     * @param yCoeff base edge line slope coefficient y
     * @param arrowLength line length.
     */
    private static void drawArrowPart(Graphics g, int x, int y, double alpha, double xCoeff, double yCoeff, double arrowLength) {
        double xCoefUp = xCoeff * Math.cos(alpha) + yCoeff * Math.sin(alpha);
        double yCoefUp = -xCoeff * Math.sin(alpha) + yCoeff * Math.cos(alpha);
        int xx1 = (int)(x - arrowLength * xCoefUp);
        int yy1 = (int)(y - arrowLength * yCoefUp);
        g.drawLine(xx1, yy1, x, y);
    }

    /**
     * Draws node in the tree (circle filled with specified color).
     * @param g graphics to draw on
     * @param point center point of the node
     * @param color fill color
     * @param str text to draw inside node
     */
    protected static void drawNode(Graphics g, Point point, Color color, String str, boolean bold) {
        int x = point.x - RADIUS;
        int y = point.y - RADIUS;
        if (color != null) {
            g.setColor(color);
            g.fillArc(x, y, DIAMETER, DIAMETER, 0, 360);
        }
        g.setColor(COLOR_BORDER);
        Stroke savedStroke = null;
        Graphics2D g2d = null;
        if (g instanceof Graphics2D) {
            g2d = (Graphics2D)g;
            if (bold) {
                savedStroke = g2d.getStroke();
                g2d.setStroke(new BasicStroke(2f));
            }
        }
        g.drawArc(x, y, DIAMETER, DIAMETER, 0, 360);
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
        g.drawString(str, (int)(point.x - bounds.getWidth() / 2),
                (int)(point.y - bounds.getY() - bounds.getHeight() / 2));
        if (savedStroke != null) {
            g2d.setStroke(savedStroke);
        }
    }

    /**
     * �������� ������ ������������ ������
     */
    public static void selectCell(JLabel cell, Color color) {
        cell.setOpaque(true);
        cell.setBackground(color);
        cell.revalidate();
        cell.repaint();
    }
}

/*
 * $Log: DrawerUtils.java,v $
 * Revision 1.1  2006/02/18 06:37:44  matvey
 * HeapSort is committed
 *
 * Revision 1.1  2005/09/10 11:31:41  matvey
 * After moving to new library
 *
 * Revision 1.1  2005/04/23 06:45:05  matvey
 * Second revision
 *
 */