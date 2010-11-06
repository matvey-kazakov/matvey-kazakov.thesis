// $Id: AutomatonActivityListener.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 06.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * @author Matvey Kazakov
 */
public interface AutomatonActivityListener {
    void stateChanged(long source, int oldState, int newState);
    void actionPerformed(long source, long action);
    void eventReceived(long source, long event);
    void eventSent(long source, long event);
}

/*
 * $Log: AutomatonActivityListener.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */