// $Id: HeapSortAutomaton.java,v 1.1 2006/02/18 06:37:44 matvey Exp $
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

/**
 * Realises all logic of the visualization automaton
 *
 * @author Matvey Kazakov
 */
public class HeapSortAutomaton extends Automaton {

    // Zeds = actions that automaton performs
    public static final int Z0_DRAW_PICTURE = 0;
    public static final int ZA0_DRAW_PICTURE = 1;

    // Factory for creating events
    private static final ASObjectFactory factory = DefaultASObjectFactory.getInstance();

    // Es = events that automaton can handle/send
    public static final long E0_NEXT_STEP = factory.getObject(AS_EVENT, EVT_HEAPSORT_NEXTSTEP);
    public static final long E4_FINISH = factory.getObject(AS_EVENT, EVT_HEAPSORT_FINISH);
    public static final long EA1_START_ANIMATION = factory.getObject(AS_EVENT, EVT_ANIMATION_START);
    public static final long EA2_STOP_ANIMATION = factory.getObject(AS_EVENT, EVT_ANIMATION_STOP);
    public static final long EA3_ANIMATION_FINISHED = factory.getObject(AS_EVENT, EVT_ANIMATION_FINISH);

    // eXes = external variables
    public static final int XA1_ANIMATION_IS_ON = 1;

    // link to global variables. This automaton can change them
    private Globals g;

    /**
     * Constructor of the heap-sort visualization automaton.
     *
     * @param g global variables that will be used by this automaton
     */
    public HeapSortAutomaton(Globals g) {
        super(VISIO_AUTOMATON_ID);
        this.g = g;
    }

    /**
     * Automaton transition realization. Uses big SWITCH - like SWITCH technology proposes.
     *
     * @param event event that this autmaton receives
     * @return <code>true</code> if transision was performed (even loop one),
     *         <code>false</code> if current state does not understand given event
     */
    protected boolean transit(long event) {
        switch (state) {
            case Y0_START:
                // Start (initial) state
                if (event == E0_NEXT_STEP) {
                    state = Y1_INIT;
                } else {
                    return false;
                }
                break;
            case Y1_INIT:
                // Initialization state = main loop state
                if (event == E0_NEXT_STEP && g.usize > 0) {
                    state = Y2_NEXT;
                } else if (event == E0_NEXT_STEP && g.hsize > 1) {
                    state = Y3_NEXT;
                } else if (event == E0_NEXT_STEP) {
                    state = Y8_FINISH;
                } else {
                    return false;
                }
                break;
            case Y2_NEXT:
                // next step state on the first algorithm stage
                state = Y5_ORDER;
                break;
            case Y3_NEXT:
                // next step state on the second stage (remove last node from the tree)
                if (event == E0_NEXT_STEP) {
                    state = _Y4_TRANSFER_START;
                } else {
                    return false;
                }
                break;
            case _Y4_TRANSFER_START:
                // exchange last and first nodes in the tree - before animation
                if (getInput(XA1_ANIMATION_IS_ON)) {
                    state = Y4A_TRANSFER_ANIMATION;
                } else {
                    state = Y4_TRANSFER;
                }
                break;
            case Y4A_TRANSFER_ANIMATION:
                // exchange last and first nodes in the tree - animation start
                if (event == E0_NEXT_STEP || event == EA3_ANIMATION_FINISHED) {
                    this.doEvent(EA2_STOP_ANIMATION);
                    state = Y4_TRANSFER;
                } else {
                    return false;
                }
                break;
            case Y4_TRANSFER:
                // exchange last and first nodes in the tree - animation finish
                if (event == E0_NEXT_STEP) {
                    state = Y5_ORDER;
                } else {
                    return false;
                }
                break;
            case Y5_ORDER:
                // start ordering elements
                if (event == E0_NEXT_STEP) {
                    state = Y6_MAX;
                } else {
                    return false;
                }
                break;
            case Y6_MAX:
                // search formaximum element
                if (event == E0_NEXT_STEP && g.cmax != g.c) {
                    state = _Y7_SWAP_START;
                } else if (event == E0_NEXT_STEP && g.usize > 0) {
                    state = Y2_NEXT;
                } else if (event == E0_NEXT_STEP && g.hsize > 1) {
                    state = Y3_NEXT;
                } else if (event == E0_NEXT_STEP) {
                    state = Y8_FINISH;
                } else {
                    return false;
                }
                break;
            case _Y7_SWAP_START:
                // exchange misarranged elements - before animation
                if (getInput(XA1_ANIMATION_IS_ON)) {
                    state = Y7A_SWAP_ANIMATION;
                } else {
                    state = Y7_SWAP;
                }
                break;
            case Y7A_SWAP_ANIMATION:
                // exchange misarranged elements - animation start
                if (event == E0_NEXT_STEP || event == EA3_ANIMATION_FINISHED) {
                    this.doEvent(EA2_STOP_ANIMATION);
                    state = Y7_SWAP;
                } else {
                    return false;
                }
                break;
            case Y7_SWAP:
                // exchange misarranged elements - animation finish
                if (event == E0_NEXT_STEP) {
                    state = Y6_MAX;
                } else {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * Performs actions in the state
     */
    protected void action() {
        switch (state) {
            case Y0_START:
                // Start (initial) state
                doAction(Z0_DRAW_PICTURE);
                break;
            case Y1_INIT:
                // Initialization state = main loop state
                g.hsize = g.a.length;
                g.usize = g.a.length / 2;
                doAction(Z0_DRAW_PICTURE);
                break;
            case Y2_NEXT:
                // next step state on the first algorithm stage
                g.usize--;
                doAction(Z0_DRAW_PICTURE);
                break;
            case Y3_NEXT:
                // next step state on the second stage (remove last node from the tree)
                g.hsize--;
                doAction(Z0_DRAW_PICTURE);
                break;
            case _Y4_TRANSFER_START:
                // exchange last and first nodes in the tree - before animation
                swap(0, g.hsize);
                break;
            case Y4A_TRANSFER_ANIMATION:
                // exchange last and first nodes in the tree - animation start
                doAction(ZA0_DRAW_PICTURE);
                doEvent(EA1_START_ANIMATION);
                break;
            case Y4_TRANSFER:
                // exchange last and first nodes in the tree - animation finish
                doAction(Z0_DRAW_PICTURE);
                break;
            case Y5_ORDER:
                // start ordering elements
                g.c = g.usize;
                doAction(Z0_DRAW_PICTURE);
                break;
            case Y6_MAX:
                // search formaximum element
                g.cmax = max(g.c, Globals.left(g.c), Globals.right(g.c));
                doAction(Z0_DRAW_PICTURE);
                break;
            case _Y7_SWAP_START:
                // exchange misarranged elements - before animation
                g.old_c = g.c;
                swap(g.c, g.cmax);
                g.c = g.cmax;
                break;
            case Y7A_SWAP_ANIMATION:
                // exchange misarranged elements - animation start
                doAction(ZA0_DRAW_PICTURE);
                doEvent(EA1_START_ANIMATION);
                break;
            case Y7_SWAP:
                // exchange misarranged elements - animation finish
                doAction(Z0_DRAW_PICTURE);
                break;
            case Y8_FINISH:
                // Finish state
                doAction(Z0_DRAW_PICTURE);
                doEvent(E4_FINISH);
                break;
        }
    }

    /**
     * Searches for maximum among three elements
     * @param x first element index
     * @param y second element index
     * @param z third element index
     * @return index of maximum element
     */
    private int max(int x, int y, int z) {
        int res = x;
        // first step: compare elements on x and y places
        if (y < g.hsize && g.a[y] > g.a[res]) {
            res = y;
        }
        // second step: compare elements on (maximum between x and y) and z places
        if (z < g.hsize && g.a[z] > g.a[res]) {
            res = z;
        }
        return res;
    }

    /**
     * Swaps two elements (on positions x and y) of array being sorted.
     *
     * @param x first element index
     * @param y second elelment index
     */
    private void swap(int x, int y) {
        int t = g.a[x];
        g.a[x] = g.a[y];
        g.a[y] = t;
    }

    /**
     * Checks whether state require some external event.
     * @return <code>true</code> if next transition requires event,
     * <code>false</code> if automaton can make next step automaticaly
     */
    protected boolean waitForEvent() {
        switch (state) {
            case _Y7_SWAP_START:
            case _Y4_TRANSFER_START:
            case Y2_NEXT:
                // return false for "before animation" states, because they should not be shown to  user
                return false;
            default:
                return true;
        }
    }

    /**
     * Returns initial state for simpler reinitialization of automaton.
     * @return automaton initial state.
     */
    protected int initialState() {
        return Y0_START;
    }
}

/*
 * $Log: HeapSortAutomaton.java,v $
 * Revision 1.1  2006/02/18 06:37:44  matvey
 * HeapSort is committed
 *
 */