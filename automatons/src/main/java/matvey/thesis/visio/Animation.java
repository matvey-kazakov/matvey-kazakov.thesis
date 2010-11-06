// $Id: Animation.java,v 1.1 2005/09/10 11:31:40 matvey Exp $
/**
 * Date: 18.02.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.visio;

import matvey.thesis.automatons.core.local.AbstractLocalAutomatonWorker;
import matvey.thesis.automatons.core.AutomatonEvent;
import matvey.thesis.automatons.core.AutomatonMessage;
import static matvey.thesis.visio.AnimationAutomaton.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Realizes animation automaton worker.
 *
 * @author Matvey Kazakov
 */
public class Animation extends AbstractLocalAutomatonWorker<AnimationAutomaton> {

    // constant: delay during animation phase between sequential steps
    private static final long ANIMATION_DELAY = 25;

    // current animation step
    private int step;
    // utility timer
    private Timer timer = new Timer();
    // state of the main automaton - required for drawing pictures
    private int visioAutomatonState;

    // parameter: number of steps
    private int n;
    // parameter: main automaton identifier to send messages to
    private long visioAutomatonId;
    // parameter: link to drawer
    private AnimationDrawer drawer;

    /**
     * Creates amination worker
     *
     * @param n                number of animation stes.
     * @param drawer           drawer that will be notified about required redrawings
     * @param visioAutomatonId main automaton identifier
     */
    public Animation(AnimationDrawer drawer, int n, long visioAutomatonId) {
        super(new AnimationAutomaton());
        this.drawer = drawer;
        this.visioAutomatonId = visioAutomatonId;
        this.n = n;
    }

    /**
     * Returns variables values.
     */
    public boolean input(int var) {
        switch (var) {
            case X0_STEP_N_1:
                return step < n - 1;
        }
        return false;
    }

    /**
     * Realizes animation actions.
     */
    public void action(int action) {
        switch (action) {
            case ZA1_START_TIMER:
                // Sends message to itself with delay - animation immitation is here.
                TimerTask task = new TimerTask() {
                    public void run() {
                        receiver.sendEvent(new AutomatonEvent(getId(), getId(), EAT_TIMER));
                    }
                };
                timer.schedule(task, ANIMATION_DELAY);
                break;
            case ZA0_DRAW_PICTURE:
                drawer.draw(visioAutomatonState, step);
                break;
            case Z3_STEP_0:
                step = 0;
                break;
            case Z4_STEP:
                step++;
                break;
        }
    }

    /**
     * Processes events from animation automaton
     */
    public void fire(long event) {
        if (event == EA3_ANIMATION_FINISHED) {
            receiver.sendEvent(new AutomatonEvent(getId(), visioAutomatonId, event));
        }
    }

    /**
     * Overrides message receiption in rder to extract additional parameter from message.
     */
    public void sendEvent(AutomatonEvent event) {
        if (event.event == EA1_START_ANIMATION && event instanceof AutomatonMessage) {
            // if start animation event is received then it must contain animation parameter
            // - current main automaton state
            AutomatonMessage<Integer> message = (AutomatonMessage<Integer>) event;
            this.visioAutomatonState = message.getObject().intValue();
        }
        super.sendEvent(event);
    }

}

/*
 * $Log: Animation.java,v $
 * Revision 1.1  2005/09/10 11:31:40  matvey
 * After moving to new library
 *
 * Revision 1.1  2005/04/23 06:45:02  matvey
 * Second revision
 *
 */