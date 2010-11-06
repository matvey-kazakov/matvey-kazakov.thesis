// $Id: AutomatonWorker.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 06.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * This interface lists all connection poits between automaton and outter world.  
 * 
 * @author Matvey Kazakov
 */
public interface AutomatonWorker extends AutomatonEventReceiver, AutomatonActivitySupplier {
    /**
     * Called by automaton to notify worker about outgoing event (E).
     * Worker is responsible for delivering event and all required parameters to recepient. 
     * @param event event that should be delivered somewhere. 
     */
    void fire(long event);

    /**
     * Called by automaton to notify worker about outgoing action (Z).
     * Worker is responsible for performing action.
     * @param action action identifier
     */
    void action(int action);

    /**
     * Called by automaton to receive external variable value (X). 
     * @param var variable identifier
     * @return boolean value of variable
     */
    boolean input(int var);

    /**
     * Sets receiver for events from automatons.
     * Worker must send all messages to this receiver if it wants to sent messages to the outter world. 
     * @param receiver receiver that will receive all messages fro automaton. Usually it is some 
     * {@link AutomatonEventHub} or something similar.
     */
    void plug(AutomatonEventReceiver receiver);

    /**
     * Called to notify worker about full system reset.
     */
    void reset();
}

/*
 * $Log: AutomatonWorker.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */