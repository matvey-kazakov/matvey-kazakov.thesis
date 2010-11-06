// $Id: HeapSortAutomatonContext.java,v 1.1 2006/02/18 06:37:45 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

import matvey.thesis.automatons.core.AutomatonEvent;
import matvey.thesis.automatons.core.AutomatonMessage;
import matvey.thesis.automatons.core.local.AbstractLocalAutomatonWorker;
import matvey.thesis.visio.AnimationDrawer;
import static matvey.thesis.visio.heapsort.HeapSortAutomaton.*;

/**
 * Implements Worker for for {@link matvey.visio.treetraversal.animation.TreeTraversalAutomaton}.
 *
 * @author Matvey Kazakov
 */
public class HeapSortAutomatonContext extends AbstractLocalAutomatonWorker<HeapSortAutomaton> {
    private AnimationDrawer drawer;
    private Globals g;
    private long animationAutomatonId;
    private long uiAutomatonId;

    /**
     * Constructs context with given linked objects.
     *
     * @param g global variables
     * @param drawer drawer that will perform slides painting
     */
    public HeapSortAutomatonContext(Globals g, HeapSortDrawer drawer) {
        super(new HeapSortAutomaton(g));
        this.drawer = drawer;
        this.g = g;
    }

    /**
     * Links this automaton with others.
     * This method receives ids of automatons that it will interract to in order
     * to have possibility to send them messages.
     * @param animationAutomatonId animation automaton identifier
     * @param uiAutomatonId user interface automaton identifier
     */
    public void setLinkedAutomatons(long animationAutomatonId, long uiAutomatonId) {
        this.animationAutomatonId = animationAutomatonId;
        this.uiAutomatonId = uiAutomatonId;
    }

    /**
     * Processes events received from automaton.
     */
    public void fire(long event) {
        if (event == EA1_START_ANIMATION) {
            // Sending satet to animation in order to setup it for correct slide painting
            receiver.sendEvent(new AutomatonMessage<Integer>(getId(), animationAutomatonId, event, new Integer(automaton.getState())));
            receiver.sendEvent(new AutomatonEvent(getId(), uiAutomatonId, event));
        } else if (event == EA2_STOP_ANIMATION) {
            receiver.sendEvent(new AutomatonEvent(getId(), animationAutomatonId, event));
            receiver.sendEvent(new AutomatonEvent(getId(), uiAutomatonId, event));
        } else if (event == E4_FINISH) {
            receiver.sendEvent(new AutomatonEvent(getId(), uiAutomatonId, event));
        }
    }

    /**
     * Processes actions received from automaton.
     */
    public void action(int action) {
        // perform action in accordance with Automaton scheme
        switch (action) {
            case Z0_DRAW_PICTURE:
                drawer.draw(automaton.getState());
                break;
            case ZA0_DRAW_PICTURE:
                drawer.draw(automaton.getState(), 0);
                break;
        }
    }

    /**
     * Returns external variables values.
     */
    public boolean input(int var) {
        switch (var) {
            case XA1_ANIMATION_IS_ON:
                return g.animationEnabled;
        }
        // no such variable
        return false;
    }

}

/*
 * $Log: HeapSortAutomatonContext.java,v $
 * Revision 1.1  2006/02/18 06:37:45  matvey
 * HeapSort is committed
 *
 * Revision 1.3  2005/09/18 15:03:28  matvey
 * X2-X4 are removed.
 * Now automaton checks global variables by itself.
 *
 * Revision 1.2  2005/09/18 14:01:19  matvey
 * Z2-Z7 are removed.
 * Now automaton changed global variables by itself.
 *
 * Revision 1.1  2005/09/10 11:31:41  matvey
 * After moving to new library
 *
 * Revision 1.1  2005/04/23 06:45:06  matvey
 * Second revision
 *
 */