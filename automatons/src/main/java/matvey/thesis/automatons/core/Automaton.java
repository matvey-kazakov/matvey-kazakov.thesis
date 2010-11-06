// $Id: Automaton.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 06.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * @author Matvey Kazakov
 */
public abstract class  Automaton implements AutomatonActivitySupplier {
    protected long id;
    
    public static final int NO_EVENT = -1;
    
    /**
     * ��������� ��������.
     */
    protected int state;
    /**
     * �������� ��������.
     */
    private AutomatonWorker worker;
    
    private final DefaultAutomatonActivitySupplier eventSupplier = new DefaultAutomatonActivitySupplier();

    protected Automaton(String sid) {
        this.id = AutomatonIdFactory.getInstance().getId(sid);
        reset();
    }

    public long getId() {
        return id;
    }

    public void setWorker(AutomatonWorker worker) {
        this.worker = worker;
    }

    /**
     * Performs automaton transision. This method must change {@link #state} variable,
     * and perform actions on transitions.
     *
     * @param event event identifier to process
     * @return <code>true</code> if transition was performed,
     *         ���� <code>false</code> if event was ignored
     */
    protected abstract boolean transit(long event);

    /**
     * ������������ �������� �������� � �������.
     */
    protected abstract void action();

    /**
     * �������� � ���, ��� ������� ���� �������� �������.
     *
     * @return <code>true</code> ���� ������� ���������, <code>false</code> ���� ���.
     */
    protected abstract boolean waitForEvent();
    
    public void reset() {
        state = initialState();
    }
    
    protected int initialState() {
        return 0;
    }

    /**
     * ��������� ��������� �������.
     * � ������ ������������� ��������� ���� ��� �������������.
     *
     * @param event �������, ������� ������ ���� ����������. ������ ������� ������.
     */
    public void run(long event) {
        long nextEvent = event;
        int oldState;
        eventSupplier.fireEventReceived(id, event);
        do {
            oldState = state;
            if (transit(nextEvent)) {
                eventSupplier.fireStateChanged(id, oldState, state);
                action();
            }
            nextEvent = NO_EVENT;
        } while (!waitForEvent());
    }
    
    protected void doAction(int action) {
        eventSupplier.fireActionPerformed(id, action);
        worker.action(action);
    }
    
    protected void doEvent(long event) {
        eventSupplier.fireEventSent(id, event);
        worker.fire(event);
    }
    
    protected boolean getInput(int id) {
        return worker.input(id);
    }
    
    /* ================================================================ */
    public void addActivityListener(AutomatonActivityListener listener) {
        eventSupplier.addActivityListener(listener);
    }

    public void removeActivityListener(AutomatonActivityListener listener) {
        eventSupplier.removeActivityListener(listener);
    }

    public int getState() {
        return state;
    }

}

/*
 * $Log: Automaton.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */