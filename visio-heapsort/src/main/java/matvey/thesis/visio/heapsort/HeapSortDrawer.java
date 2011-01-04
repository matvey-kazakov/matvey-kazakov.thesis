// $Id: HeapSortDrawer.java,v 1.1 2006/02/18 06:37:45 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

import matvey.thesis.visio.AnimationDrawer;
import static matvey.thesis.visio.heapsort.HeapSortAutomatonConstants.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Drawer performs transformation between visualization automation state and picture shown to user.
 *
 * @author Matvey Kazakov
 */
public class HeapSortDrawer extends JPanel implements AnimationDrawer {

    // link to global variables
    private Globals globals;
    // label with text message
    private JLabel messageLabel;

    //  total width of the tree
    private int treeWidth;
    //  total heigh of the tree
    private int treeHeight;
    // coordinates of nodes
    private Point[] points;
    // panel contains picture of tree
    private GraphPanel treePanel;
    // labels representing array
    private JLabel[] array;

    // special thread for drawing
    private DrawingThread drawingThread = new DrawingThread();
    // current state (copied to the drawer for thread safety)
    private int drawerState = 0;
    // current animation step (copied to the drawer for thread safety)
    private int drawerStep = 0;
    // lock object for reading/writing drawer state and step variables
    private Object drawerLock = new Object();
    // current selected cells in the array
    private Integer[] selectedCells = null;

    /**
     * Constructs drawer.
     * @param globals global variables reference
     */
    public HeapSortDrawer(Globals globals) {
        this.globals = globals;
        // initialize node corrdinates
        calculateCoordinates();
        // create outlook stub
        array = new JLabel[globals.a.length];
        setLayout(new BorderLayout());
        JPanel messagePanel = new JPanel(new BorderLayout());
        JPanel picturePanel = new JPanel(new BorderLayout());
        treePanel = new GraphPanel();
        picturePanel.add(this.treePanel, BorderLayout.CENTER);
        add(picturePanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);
        picturePanel.add(DrawerUtils.createArrayPanel("Массив", BoxLayout.X_AXIS, array, true), BorderLayout.SOUTH);
        // update array with up-to-date values
        updateArray();
        messageLabel = new JLabel();
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        // start drawing thread for permanent UI update
        drawingThread.start();
    }

    /**
     * Performs drawing of the given state and given animation step
     * @param s state to be painted
     * @param step animations step to be painted
     */
    public void draw(final int s, final int step) {
        // store state and animation step
        synchronized (drawerLock) {
            drawerState = s;
            drawerStep = step;
        }
        // notify drawing thread that it can perform drawing
        drawingThread.notifyDraw();
    }

    /**
     * Performs drawing of the given state
     * @param s state to be painted
     */
    public void draw(final int s) {
        // redirect call
        draw(s, -1);
    }

    /**
     * Special method for selecting given elements in the array
     * @param selected1 first selected element
     * @param selected2 second seleced element
     * @param current main selected element
     */
    private void updateArray(int selected1, int selected2, int current) {
        updateArray();
        ArrayList<Integer> selected = new ArrayList<Integer>();
        selectIfPossible(selected1, selected, DrawerUtils.COLOR_SELECT);
        selectIfPossible(selected2, selected, DrawerUtils.COLOR_SELECT);
        selectIfPossible(current, selected, DrawerUtils.COLOR_CURRENT);
        selectedCells = selected.toArray(new Integer[0]);
    }

    /**
     * Selects element in the array if its index is corect
     * @param idx index of the element
     * @param selected array of selected indices
     * @param color color to select element with
     */
    private void selectIfPossible(int idx, ArrayList<Integer> selected, Color color) {
        if (idx != -1) {
            DrawerUtils.selectCell(array[idx], color);
            selected.add(idx);
        }
    }

    /**
     * Updates current array presentation
     */
    private void updateArray() {
        if (selectedCells != null) {
            for (int i = 0; i < selectedCells.length; i++) {
                int cell = selectedCells[i];
                DrawerUtils.deselectCell(array[cell]);
            }
        }
        for (int i = 0; i < array.length; i++) {
            JLabel label = array[i];
            String newS = String.valueOf(globals.a[i]);
            Font newF = drawerState == Y8_FINISH
                    || drawerState != Y0_START && i >= globals.hsize
                    && !(drawerState == Y3_NEXT && i == globals.hsize)
                    ? DrawerUtils.boldFont : DrawerUtils.plainFont;
            if (!newS.equals(label.getText())) {
                label.setText(newS);
            }
            if (!newF.equals(label.getFont())) {
                label.setFont(newF);
            }
        }
    }

    /**
     * Changes current text message in visualizer + updates selection.
     * For example, it can select
     *
     * @param state state that message should be created for
     */
    private void changeMessage(int state) {
        String newMessage = null;
        switch (state) {
            case Y0_START:
                newMessage = "Начальное состояние. Расставляем элементы в виде дерева.";
                break;
            case Y1_INIT:
                newMessage = "Инициализация. Соединяем вершины ребрами.";
                break;
            case Y3_NEXT:
                newMessage = "Исключаем последний элемент из дерева.";
                break;
            case Y4_TRANSFER:
            case Y4A_TRANSFER_ANIMATION:
                newMessage = "Обмениваем его с вершиной. Элемент " + globals.a[globals.hsize] + " на месте.";
                break;
            case Y5_ORDER:
                newMessage = "Упорядочивание. Ищем место для вершины " + globals.a[globals.c];
                break;
            case Y6_MAX:
                newMessage = "Сравниваем с правым и левым потомком. Максимум равен " + globals.a[globals.cmax];
                break;
            case Y7A_SWAP_ANIMATION:
            case Y7_SWAP:
                newMessage = "Обмениваем с максимумом";
                break;
            case Y8_FINISH:
                newMessage = "Конец. Массив упорядочен.";
        }
        // updating label
        if (newMessage != null) {
            messageLabel.setText(newMessage);
        }
    }

    /**
     * Возвращает глубину дерева
     */
    private void calculateCoordinates() {
        ArrayList<Point> result = new ArrayList<Point>();
        int rowDistanse = DrawerUtils.DISTANCE;
        int numberOfRows = (int)Math.floor(Math.sqrt(globals.a.length)) + 1;
        int maxRowLength = (int)Math.pow(2, numberOfRows - 1);
        treeWidth = (maxRowLength - 1) * DrawerUtils.DISTANCE + DrawerUtils.RADIUS * 2;
        treeHeight = (numberOfRows - 1) * rowDistanse + DrawerUtils.RADIUS * 2;
        int number = 1;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < number; j++) {
                result.add(new Point(DrawerUtils.RADIUS + treeWidth * (2 * j + 1) / number / 2,
                        DrawerUtils.RADIUS + i * rowDistanse + DrawerUtils.RADIUS));
            }
            number *= 2;
        }
        points = (Point[])result.toArray(new Point[]{});
    }

    /**
     * Special method that updates drawer.
     */
    public void init() {
        updateArray();
    }

    private void performDraw() {
        // Убираем выделение
        int s, step;
        synchronized (drawerLock) {
            s = drawerState;
            step = drawerStep;
        }
        // Перерисовываем общую картину
        treePanel.repaint(s, step);
        changeMessage(s);
    }

    /**
     * Contains graphical representation of tree + methods for drawing.
     */
    private class GraphPanel extends JPanel {
        private int prefferedWidth;
        private int prefferedHeight;
        private int step;
        private int state;

        /**
         * Constructs panel.
         */
        public GraphPanel() {
            prefferedWidth = treeWidth + DrawerUtils.DIAMETER;
            prefferedHeight = treeHeight + DrawerUtils.DIAMETER * 3 / 2;
            setPreferredSize(new Dimension(prefferedWidth, prefferedHeight));
            setBackground(DrawerUtils.COLOR_BACKGROUND);
        }

        /**
         * Performs repaining tree.
         */
        public void paint(Graphics g) {
            super.paint(g);
            Point[] points = shiftPoints();

            int current = -1;
            int choice = -1;
            int selected1 = -1;
            int selected2 = -1;
            switch (state) {
                case Y3_NEXT:
                    current = 0;
                    selected1 = globals.hsize;
                    break;
                case Y4_TRANSFER:
                    selected1 = 0;
                    current = globals.hsize;
                    break;
                case Y5_ORDER:
                    current = globals.c;
                    break;
                case Y6_MAX:
                    current = globals.c;
                    int left = Globals.left(globals.c);
                    selected1 = left < globals.hsize ? left : -1;
                    int right = Globals.right(globals.c);
                    selected2 = right < globals.hsize ? right : -1;
                    choice = globals.cmax;
                    break;
                case Y7_SWAP:
                    current = globals.c;
                    selected1 = globals.old_c;
                    break;
            }
            updateArray(selected1, selected2, current);
            int step = this.step;
            // drawing nodes
            for (int i = 0; i < points.length; i++) {
                Point point = points[i];
                Color color = DrawerUtils.COLOR_BACKGROUND;
                if (i == selected1 || i == selected2) {
                    color = DrawerUtils.COLOR_SELECT;
                }
                if (i == current) {
                    color = DrawerUtils.COLOR_CURRENT;
                }
                DrawerUtils.drawNode(g, point, color,
                        state == Y7A_SWAP_ANIMATION && (i == globals.c || i == globals.old_c)
                                || state == Y4A_TRANSFER_ANIMATION && (i == 0 || i == globals.hsize)
                                ? "" : String.valueOf(globals.a[i]), choice == i, i);
            }
            drawEdges(g, points);
            drawAnimation(points, step, g);
        }

        private void drawAnimation(Point[] points, int step, Graphics g) {
            // drawing animation part
            if (state == Y7A_SWAP_ANIMATION || state == Y4A_TRANSFER_ANIMATION) {
                int fromIdx = state == Y7A_SWAP_ANIMATION ? globals.old_c : 0;
                int toIdx = state == Y7A_SWAP_ANIMATION ? globals.c : globals.hsize;
                Point from = points[fromIdx];
                Point to = points[toIdx];
                double distance = Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y)) - DrawerUtils.DIAMETER;
                if (distance > 0) {
                    double currentDistance = distance / globals.animationNsteps * step;
                    double xCoeff = (to.x - from.x) / distance;
                    double yCoeff = (to.y - from.y) / distance;
                    double deltaX = currentDistance * xCoeff;
                    double deltaY = currentDistance * yCoeff;
                    int x1 = (int)(from.x + deltaX);
                    int y1 = (int)(from.y + deltaY);
                    int x2 = (int)(to.x - deltaX);
                    int y2 = (int)(to.y - deltaY);
                    DrawerUtils.drawNode(g, new Point(x1, y1), DrawerUtils.COLOR_CURRENT,
                            String.valueOf(globals.a[toIdx]), false, -1);
                    DrawerUtils.drawNode(g, new Point(x2, y2), DrawerUtils.COLOR_SELECT,
                            String.valueOf(globals.a[fromIdx]), false, -1);
                }
            }
        }

        private void drawEdges(Graphics g, Point[] points) {
            // drawing edges
            for (int i = 0; i < globals.hsize / 2; i++) {
                int left = Globals.left(i);
                int right = Globals.right(i);
                if (globals.hsize > left) {
                    DrawerUtils.drawArrow(g, points[i], points[left]);
                }
                if (globals.hsize > right) {
                    DrawerUtils.drawArrow(g, points[i], points[right]);
                }
            }
        }

        /**
         * Shifts points upon current panel size
         * @return array of shifted node coordinates
         */
        private Point[] shiftPoints() {
            Dimension size = getSize();
            int shiftX = (size.width - prefferedWidth) / 2;
            int shiftY = (size.height - prefferedHeight) / 2 + DrawerUtils.DIAMETER / 2;
            // moving point to position tree in the center of the panel
            Point[] points = new Point[HeapSortDrawer.this.points.length];
            for (int i = 0; i < points.length; i++) {
                Point originalPoint = HeapSortDrawer.this.points[i];
                Point point = new Point(originalPoint);
                point.translate(shiftX, shiftY);
                points[i] = point;
            }
            return points;
        }

        /**
         * Redraws in accordance with given stateand step.
         *
         * @param state main automaton state number
         * @param step  animation step. &lt;0 means no animation.
         */
        protected void repaint(int state, int step) {
            this.step = step;
            this.state = state;
            repaint();
        }
    }

    /**
     * Performs drawing. Special thread to decompose drawing from visualizer.
     * It removes unnecessary lags during drawing.
     */
    private class DrawingThread extends Thread {
        /**
         * Constructs thread
         */
        public DrawingThread() {
            super("Drawing Thread");
            setDaemon(true);
        }
        
        boolean needDraw = false;

		/**
		 * Notify about redraw
		 */
        public void notifyDraw() {
        	synchronized (this) {
            	needDraw = true;
            	notify();
			}
		}

		/**
         * Performs drawing cycle.
         */
        public void run() {
            while (!interrupted()) {
                synchronized (this) {
                	if (!needDraw) {
	                    // waiting for changes
	                    try {
	                        wait();
	                    } catch (InterruptedException e) {
	                        break;
	                    }
                	}
                    // redraw picture
                    performDraw();
            		needDraw = false;
                }
            }
        }
    }
}

/*
 * $Log: HeapSortDrawer.java,v $
 * Revision 1.1  2006/02/18 06:37:45  matvey
 * HeapSort is committed
 *
 * Revision 1.1  2005/09/10 11:31:41  matvey
 * After moving to new library
 *
 * Revision 1.1  2005/04/23 06:45:06  matvey
 * Second revision
 *
 * Revision 1.1  2005/02/05 13:56:36  matvey
 * First revision
 *
 */