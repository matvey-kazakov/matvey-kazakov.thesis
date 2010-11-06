// $Id: AnimationAutomaton.java,v 1.1 2005/09/10 11:31:40 matvey Exp $
/**
 * Date: 07.01.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.visio;

import matvey.thesis.automatons.core.ASObjectFactory;
import static matvey.thesis.automatons.core.ASObjectType.AS_EVENT;
import matvey.thesis.automatons.core.Automaton;
import matvey.thesis.automatons.core.DefaultASObjectFactory;
import static matvey.thesis.visio.AnimationAutomatonConstants.*;


/**
 * Realizes animation automaton.
 *
 * @author Matvey Kazakov
 */
public class AnimationAutomaton extends Automaton {

    private static final ASObjectFactory factory = DefaultASObjectFactory.getInstance();

    public static final long EAT_TIMER = factory.getObject(AS_EVENT, EVT_ANIMATION_TIMER);
    public static final long EA1_START_ANIMATION = factory.getObject(AS_EVENT, EVT_ANIMATION_START);
    public static final long EA2_STOP_ANIMATION = factory.getObject(AS_EVENT, EVT_ANIMATION_STOP);
    public static final long EA3_ANIMATION_FINISHED = factory.getObject(AS_EVENT, EVT_ANIMATION_FINISH);

    public static final int ZA0_DRAW_PICTURE = 0;
    public static final int ZA1_START_TIMER = 1;

    public static final int YA0_START = 0;
    public static final int YA1_STEP = 1;
    public static final int YA2_NEXT = 2;

    public static final int Z3_STEP_0 = 3;
    public static final int Z4_STEP = 4;

    public static final int X0_STEP_N_1 = 0;


    /**
     * Createsanimation automaton.
     */
    public AnimationAutomaton() {
        super(ANIMATION_AUTOMATON_ID);
    }

    /**
     * Filters states that require external events.
     */
    protected boolean waitForEvent() {
        switch (state) {
            case YA0_START:
            case YA2_NEXT:
                return true;
            case YA1_STEP:
                return false;
        }
        return true;
    }


    /**
     * Returns initial state.
     */
    protected int initialState() {
        return YA0_START;
    }

    /**
     * Performs animationautomaton transision
     */
    protected boolean transit(long event) {
        switch (state) {
            case YA0_START:
                // animation is started
                if (event == EA1_START_ANIMATION) {
                    doAction(Z3_STEP_0);
                    state = YA1_STEP;
                } else {
                    return false;
                }
                break;
            case YA1_STEP:
                // animation step is performed
                if (getInput(X0_STEP_N_1)) {
                    state = YA2_NEXT;
                } else {
                    doEvent(EA3_ANIMATION_FINISHED);
                    state = YA0_START;
                }
                break;
            case YA2_NEXT:
                // animation is in pause between steps
                if (event == EA2_STOP_ANIMATION) {
                    state = YA0_START;
                } else if (event == EAT_TIMER) {
                    state = YA1_STEP;
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
     * Performs animation actions.
     */
    protected void action() {
        switch (state) {
            case YA1_STEP:
                doAction(Z4_STEP);
                doAction(ZA0_DRAW_PICTURE);
                break;
            case YA2_NEXT:
                doAction(ZA1_START_TIMER);
                break;
        }
    }
}

/*
 * $Log: AnimationAutomaton.java,v $
 * Revision 1.1  2005/09/10 11:31:40  matvey
 * After moving to new library
 *
 */