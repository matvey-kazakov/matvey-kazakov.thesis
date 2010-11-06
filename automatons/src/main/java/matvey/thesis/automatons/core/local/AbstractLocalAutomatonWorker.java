// $Id: AbstractLocalAutomatonWorker.java,v 1.1 2006/02/18 07:00:32 matvey Exp $
/**
 * Date: 14.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core.local;

import matvey.thesis.automatons.core.*;

/**
 * @author Matvey Kazakov
 */
public abstract class AbstractLocalAutomatonWorker<T extends Automaton> implements AutomatonWorker {
    protected AutomatonEventReceiver receiver;
    protected T automaton;

    protected AbstractLocalAutomatonWorker(T automaton) {
        this.automaton = automaton;
        this.automaton.setWorker(this);
        receiver = new DummyReceiver();
    }

    public void plug(AutomatonEventReceiver receiver) {
        this.receiver = receiver;
    }

    public void reset() {
        automaton.reset();
    }

    public void sendEvent(AutomatonEvent event) {
        if (event.dest == automaton.getId()) {
            automaton.run(event.event);
        }
    }

    public long getId() {
        return automaton.getId();
    }

    public void addActivityListener(AutomatonActivityListener listener) {
        automaton.addActivityListener(listener);
    }

    public void removeActivityListener(AutomatonActivityListener listener) {
        automaton.removeActivityListener(listener);
    }
}

/*
 * $Log: AbstractLocalAutomatonWorker.java,v $
 * Revision 1.1  2006/02/18 07:00:32  matvey
 * just added
 *
 */