// $Id: UIAutomaton.java,v 1.1 2006/02/18 06:37:45 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

import matvey.thesis.automatons.core.ASObjectFactory;
import static matvey.thesis.automatons.core.ASObjectType.AS_EVENT;
import matvey.thesis.automatons.core.Automaton;
import matvey.thesis.automatons.core.DefaultASObjectFactory;
import static matvey.thesis.visio.AnimationAutomatonConstants.*;
import static matvey.thesis.visio.heapsort.HeapSortAutomatonConstants.*;
import static matvey.thesis.visio.heapsort.UIAutomatonConstants.*;

/**
 * User interface automaton
 *
 * @author Matvey Kazakov
 */
class UIAutomaton extends Automaton {

    // states
    public static final int Y11_STEP = 11;
    public static final int Y12_AUTO = 12;
    public static final int Y13_PAUSE = 13;
    public static final int Y14_FINISH = 14;

    private static final ASObjectFactory factory = DefaultASObjectFactory.getInstance();

    // events
    public static final long E10_AUTO_STOP = factory.getObject(AS_EVENT, EVT_UI_AUTO_STOP);
    public static final long E11_RESET = factory.getObject(AS_EVENT, EVT_UI_RESET);
    public static final long T10_TIMER = factory.getObject(AS_EVENT, EVT_UI_TIMER);
    public static final long E0_NEXT_STEP = factory.getObject(AS_EVENT, EVT_HEAPSORT_NEXTSTEP);
    public static final long E4_FINISH = factory.getObject(AS_EVENT, EVT_HEAPSORT_FINISH);
    public static final long EA1_START_ANIMATION = factory.getObject(AS_EVENT, EVT_ANIMATION_START);
    public static final long EA2_STOP_ANIMATION = factory.getObject(AS_EVENT, EVT_ANIMATION_STOP);

    /**
     * Constructs automaton
     */
    public UIAutomaton() {
        super(UI_AUTOMATON_ID);
    }

    /**
     * Performs automaton transition
     */
    protected boolean transit(long event) {
        switch (state) {
            // step-by-step state
            case Y11_STEP:
                if (event == E4_FINISH) {
                    state = Y14_FINISH;
                } else if (event == E11_RESET) {
                    doAction(Z10_INIT);
                } else if (event == E10_AUTO_STOP) {
                    doAction(Z11_TURN_AUTO_ON);
                    state = Y12_AUTO;
                } else {
                    return false;
                }
                break;
            // automatic mode
            case Y12_AUTO:
                if (event == EA1_START_ANIMATION) {
                    state = Y13_PAUSE;
                } else if (event == E4_FINISH) {
                    doAction(Z12_TURN_AUTO_OFF);
                    state = Y14_FINISH;
                } else if (event == E10_AUTO_STOP) {
                    doAction(Z12_TURN_AUTO_OFF);
                    state = Y11_STEP;
                } else if (event == T10_TIMER) {
                    doEvent(E0_NEXT_STEP);
                } else {
                    return false;
                }
                break;
            // paused state
            case Y13_PAUSE:
                if (event == EA2_STOP_ANIMATION) {
                    state = Y12_AUTO;
                } else if (event == E10_AUTO_STOP) {
                    doAction(Z12_TURN_AUTO_OFF);
                    state = Y11_STEP;
                } else {
                    return false;
                }
                break;
            // end of visualization
            case Y14_FINISH:
                if (event == E11_RESET) {
                    doAction(Z13_SWITCH_NEXT_ON);
                    doAction(Z10_INIT);
                    state = Y11_STEP;
                } else {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Performs action in the state
     */
    protected void action() {
        switch (state) {
            case Y14_FINISH:
                doAction(Z14_SWITCH_NEXT_OFF);
                break;
            case Y12_AUTO:
                doAction(Z15_START_TIMER);
                break;
        }

    }

    /**
     * Always waits for event (pure reactive automaton).
     */
    protected boolean waitForEvent() {
        return true;
    }

    /**
     * Answers initial state
     */
    protected int initialState() {
        return Y11_STEP;
    }

}

/*
 * $Log: UIAutomaton.java,v $
 * Revision 1.1  2006/02/18 06:37:45  matvey
 * HeapSort is committed
 *
 * Revision 1.1  2005/09/10 11:31:42  matvey
 * After moving to new library
 *
 * Revision 1.1  2005/04/23 06:45:06  matvey
 * Second revision
 *
 */