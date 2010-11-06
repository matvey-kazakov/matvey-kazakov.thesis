// $Id: RWLock.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 07.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.utils;

/**
 * @author Matvey Kazakov
 */
public class RWLock {
    private int givenLocks;
    private int waitingWriters;

    private Object mutex;


    public RWLock() {
        mutex = new Object();
        givenLocks = 0;
        waitingWriters = 0;
    }

    public void getReadLock() {
        synchronized (mutex) {
            try {
                while ((givenLocks == -1) || (waitingWriters != 0)) {
                    mutex.wait();
                }
            }
            catch (InterruptedException e) {
            }
            givenLocks++;
        }
    }

    public void getWriteLock() {
        synchronized (mutex) {
            waitingWriters++;
            try {
                while (givenLocks != 0) {
                    mutex.wait();
                }
            }
            catch (InterruptedException e) {
            }
            waitingWriters--;
            givenLocks = -1;
        }
    }

    public void releaseLock() {
        synchronized (mutex) {
            if (givenLocks == 0) {
                return;
            }
            if (givenLocks == -1) {
                givenLocks = 0;
            } else {
                givenLocks--;
            }
            mutex.notifyAll();
        }
    }
}

/*
 * $Log: RWLock.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */