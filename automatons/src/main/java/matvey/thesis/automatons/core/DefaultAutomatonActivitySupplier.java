// $Id: DefaultAutomatonActivitySupplier.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 07.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Matvey Kazakov
 */
public class DefaultAutomatonActivitySupplier implements AutomatonActivitySupplier {
    
    private List<AutomatonActivityListener> listeners = new ArrayList<AutomatonActivityListener>();
    
    public synchronized void addActivityListener(AutomatonActivityListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeActivityListener(AutomatonActivityListener listener) {
        listeners.remove(listener);
    }
    
    public void fireStateChanged(long source, int oldState, int newState) {
        AutomatonActivityListener[] li = listeners.toArray(new AutomatonActivityListener[]{});
        for (int i = 0; i < li.length; i++) {
            AutomatonActivityListener listener = li[i];
            listener.stateChanged(source, oldState, newState);
        }
    }
    
    public void fireEventReceived(long source, long event) {
        AutomatonActivityListener[] li = listeners.toArray(new AutomatonActivityListener[]{});
        for (int i = 0; i < li.length; i++) {
            AutomatonActivityListener listener = li[i];
            listener.eventReceived(source, event);
        }
    }
    
    public void fireActionPerformed(long source, long action) {
        AutomatonActivityListener[] li = listeners.toArray(new AutomatonActivityListener[]{});
        for (int i = 0; i < li.length; i++) {
            AutomatonActivityListener listener = li[i];
            listener.actionPerformed(source, action);
        }
    }
    
    public void fireEventSent(long source, long event) {
        AutomatonActivityListener[] li = listeners.toArray(new AutomatonActivityListener[]{});
        for (int i = 0; i < li.length; i++) {
            AutomatonActivityListener listener = li[i];
            listener.eventSent(source, event);
        }
    }
}

/*
 * $Log: DefaultAutomatonActivitySupplier.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */