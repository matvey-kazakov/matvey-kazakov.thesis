// $Id: AnimationAutomatonConstants.java,v 1.1 2005/09/10 11:31:40 matvey Exp $
/**
 * Date: 18.02.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.visio;

/**
 * Contains constants for animation automaton and worker.
 *
 * @see AnimationAutomaton
 * @see Animation
 * @author Matvey Kazakov
 */
public interface AnimationAutomatonConstants {
    // events
    String EVT_ANIMATION_TIMER = "Animation.TIMER";
    String EVT_ANIMATION_START = "Animation.START";
    String EVT_ANIMATION_STOP = "Animation.STOP";
    String EVT_ANIMATION_FINISH = "Animation.FINISH";

    // animation automaton identifier
    String ANIMATION_AUTOMATON_ID = "Animation (AA)";
}

/*
 * $Log: AnimationAutomatonConstants.java,v $
 * Revision 1.1  2005/09/10 11:31:40  matvey
 * After moving to new library
 *
 */