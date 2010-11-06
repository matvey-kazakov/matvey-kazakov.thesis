// $Id: VisioA.java,v 1.1 2006/02/18 06:37:45 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

import matvey.thesis.automatons.core.*;
import matvey.thesis.automatons.core.local.AbstractLocalAutomatonWorker;
import matvey.thesis.visio.Animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main visualizer class. Combines all logic parts into one.
 *
 * @author Matvey Kazakov
 */
public class VisioA extends JPanel implements ActionListener, HeapSortAutomatonConstants, UIAutomatonConstants {

    // Button labels
    private static final String LABEL_AUTO = "Авто";
    private static final String LABEL_STOP = "Стоп";
    private static final String LABEL_STEP = ">>";
    private static final String LABEL_RESTART = "Заново";
    private static final String LABEL_DELAYUP = ">";
    private static final String LABEL_DELAYDOWN = "<";
    private static final String LABEL_DELAY = "Пауза: {0}";
    private static final String LABEL_ANIMATION = "Анимация";

    // Buttons themselves
    private JButton buttonStep = new JButton(LABEL_STEP);
    private JButton buttonRestart = new JButton(LABEL_RESTART);
    private JButton buttonAuto = new JButton(LABEL_AUTO);
    private JButton buttonDelayDown = new JButton(LABEL_DELAYDOWN);
    private JButton buttonDelayUp = new JButton(LABEL_DELAYUP);
    private JCheckBox cbAnimation = new JCheckBox(LABEL_ANIMATION);
    private JLabel labelDelay = new JLabel();

    // Pause constants
    private static final int INITIAL_DELAY = 3;
    private static final int[] DELAYS = new int[]{50, 100, 250, 500, 1000, 2500};
	private static final boolean DEBUG = false;

    // Global variables
    private Globals globals = new Globals();

    // Visualizer automaton
    private HeapSortAutomatonContext treeTraversal;
    // User interface automaton
    private UIprocessor uiProcessor;
    // Animation automaton
    private Animation animation;
    // Event hub connects all automatons
    private AutomatonEventHub hub;

    // Drawer preparing pictures and text comments
    private HeapSortDrawer drawer = new HeapSortDrawer(globals);

    // current delay
    private int delay = INITIAL_DELAY;

    /**
     * Starts visualizer
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Пирамидальная сортировка");
        VisioA visio = new VisioA();
        frame.setContentPane(visio);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Creates visualizer outlook and initializes it
     */
    public VisioA() {
        // initialize automatons
        initAutomatons();

        setBorder(BorderFactory.createLineBorder(getBackground(), 5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Visualizer consists of drawer and buttons panel
        add(drawer);
        add(Box.createVerticalStrut(10));
        add(initButtonsPanel());
        drawer.draw(Y0_START);
    }

    /**
     * Initializes buttons panel.
     */
    private JPanel initButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        // add lister to all buttons
        buttonStep.addActionListener(this);
        buttonRestart.addActionListener(this);
        buttonAuto.addActionListener(this);
        buttonDelayDown.addActionListener(this);
        buttonDelayUp.addActionListener(this);
        cbAnimation.addActionListener(this);

        // put first group of controls
        buttonPanel.add(buttonStep);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(buttonRestart);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(Box.createHorizontalGlue());

        // put second group of controls
        buttonPanel.add(cbAnimation);
        buttonPanel.add(Box.createHorizontalStrut(10));
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
        return buttonPanel;
    }

    /**
     * Performs automatons initialization
     */
    private void initAutomatons() {
        // create automatons
        treeTraversal = new HeapSortAutomatonContext(globals, drawer);
        animation = new Animation(drawer, globals.animationNsteps, treeTraversal.getId());
        uiProcessor = new UIprocessor();

        // link automatons
        treeTraversal.setLinkedAutomatons(animation.getId(), uiProcessor.getId());
        if (DEBUG) {
            // loggers for detailed automatons activity listening
            animation.addActivityListener(new VisioProtocolWriter());
            uiProcessor.addActivityListener(new VisioProtocolWriter());
            treeTraversal.addActivityListener(new VisioProtocolWriter());
        }

        // plug in all automatons to hub
        hub = new AutomatonEventHub(1);
        plug(hub, animation);
        plug(hub, uiProcessor);
        plug(hub, treeTraversal);

        if (DEBUG) {
            // additional listener for all messages in the HUB - too heavy
            hub.addReceiver(new AutomatonEventReceiver() {
                public void sendEvent(AutomatonEvent event) {
                    System.out.println("Hub: from " + AutomatonIdFactory.getInstance().getSID(event.source)
                            + " to " + AutomatonIdFactory.getInstance().getSID(event.dest) + " "
                            + DefaultASObjectFactory.getInstance().getObjectSID(ASObjectType.AS_EVENT, event.event));
                    System.out.println("");
                }
            });
        }
    }

    /**
     * Plugs automaton to hub by setting up to way event list
     * @param hub hub to plug to
     * @param automatonWorker
     */
    private void plug(AutomatonEventHub hub, AutomatonWorker automatonWorker) {
        // automaton should listen events from others
        hub.addReceiver(automatonWorker);
        // automaton should sent events to others
        automatonWorker.plug(hub);
    }

    /**
     * Actions handler
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // on most actions this method just sends appropriate event to user interface automaton.
        if (source == buttonStep) {
            hub.sendEvent(new AutomatonEvent(0, treeTraversal.getId(), HeapSortAutomaton.E0_NEXT_STEP));
        } else if (source == buttonRestart) {
            hub.sendEvent(new AutomatonEvent(0, uiProcessor.getId(), UIAutomaton.E11_RESET));
        } else if (source == buttonAuto) {
            hub.sendEvent(new AutomatonEvent(0, uiProcessor.getId(), UIAutomaton.E10_AUTO_STOP));
        } else if (source == buttonDelayDown) {
            // decrease delay
            if (delay > 0) {
                delay--;
                updateLabelDelay();
            }
            updateDelayButtonsState();
        } else if (source == buttonDelayUp) {
            // increase delay
            if (delay < DELAYS.length - 1) {
                delay++;
                updateLabelDelay();
            }
            updateDelayButtonsState();
        } else if (source == cbAnimation) {
            // change animation flag
            globals.animationEnabled = cbAnimation.isSelected();
        }
    }

    /**
     * Performs full reset for visualizer
     */
    private void reinit() {
        globals.init();
        treeTraversal.reset();
        animation.reset();
        drawer.init();
        drawer.draw(Y0_START);
    }

    // ========== Functions that update buttons states =====================

    /**
     * Delay buttons are updated upon current delay
     */
    private void updateDelayButtonsState() {
        buttonDelayDown.setEnabled(delay > 0);
        buttonDelayUp.setEnabled(delay < DELAYS.length - 1);
    }

    /**
     * Delay label is updated upon current delay
     */
    private void updateLabelDelay() {
        labelDelay.setText(MessageFormat.format(LABEL_DELAY, new Object[]{"" + DELAYS[delay]}));
    }

    /**
     * This classs realizes user interface automaton worker.
     * Despite the fact that automaton itself is external class, this workeris inner because it
     * should have access to inner variables of {@link matvey.visio.heapsort.VisioA}.
     */
    private class UIprocessor extends AbstractLocalAutomatonWorker<UIAutomaton> {

        /**
         * Utility timer for automatic visualization stepping.
         */
        private Timer autoTimer = new Timer();


        /**
         * Constructs user interface automaton worker
         */
        public UIprocessor() {
            super(new UIAutomaton());
        }

        /**
         * Translates events from automaton
         */
        public void fire(long event) {
            if (event == UIAutomaton.E0_NEXT_STEP) {
                receiver.sendEvent(new AutomatonEvent(getId(), treeTraversal.getId(), event));
            }
        }

        /**
         * Performs actions initiated by automaton
         */
        public void action(int action) {
            switch (action) {
                case Z10_INIT:
                    reinit();
                    break;
                case Z11_TURN_AUTO_ON:
                    buttonAuto.setText(LABEL_STOP);
                    buttonRestart.setEnabled(false);
                    buttonStep.setEnabled(false);
                    break;
                case Z12_TURN_AUTO_OFF:
                    buttonAuto.setText(LABEL_AUTO);
                    buttonRestart.setEnabled(true);
                    buttonStep.setEnabled(true);
                    break;
                case Z13_SWITCH_NEXT_ON:
                    buttonStep.setEnabled(true);
                    buttonAuto.setEnabled(true);
                    break;
                case Z14_SWITCH_NEXT_OFF:
                    buttonStep.setEnabled(false);
                    buttonAuto.setEnabled(false);
                    break;
                case Z15_START_TIMER:
                    // schedule nextstep with some delay
                    TimerTask task = new TimerTask() {
                        public void run() {
                            receiver.sendEvent(new AutomatonEvent(getId(), getId(), UIAutomaton.T10_TIMER));
                        }
                    };
                    autoTimer.schedule(task, DELAYS[delay]);
                    break;
            }
        }

        /**
         * Always returns false, because there are no variables.
         */
        public boolean input(int varId) {
            return false;
        }
    }

    /**
     * Simple protocol writer
     */
    private static class VisioProtocolWriter implements AutomatonActivityListener {
        /**
         * Logs transition
         */
        public void stateChanged(long source, int oldState, int newState) {
            log(source, oldState + " -> " + newState);
        }

        // inner date format
        private DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS ");

        /**
         * Performs output
         * @param source source automaton identifier
         * @param message message to be logged
         */
        private synchronized void log(long source, String message) {
            System.out.println(format.format(new Date()) + AutomatonIdFactory.getInstance().getSID(source) + ": " + message);
        }

        /**
         * Log action performance
         */
        public void actionPerformed(long source, long action) {
            log(source, "do " + action);
        }

        /**
         * Log event reception
         */
        public void eventReceived(long source, long event) {
            log(source, "-> " + DefaultASObjectFactory.getInstance().getObjectSID(ASObjectType.AS_EVENT, event));
        }

        /**
         * Log event sending
         */
        public void eventSent(long source, long event) {
            log(source, DefaultASObjectFactory.getInstance().getObjectSID(ASObjectType.AS_EVENT, event) + " ->");
        }
    }
}

/*
 * $Log: VisioA.java,v $
 * Revision 1.1  2006/02/18 06:37:45  matvey
 * HeapSort is committed
 *
 * Revision 1.1  2005/09/10 11:31:42  matvey
 * After moving to new library
 *
 * Revision 1.2  2005/04/23 06:45:07  matvey
 * Second revision
 *
 * Revision 1.1  2005/02/05 13:56:37  matvey
 * First revision
 *
 */