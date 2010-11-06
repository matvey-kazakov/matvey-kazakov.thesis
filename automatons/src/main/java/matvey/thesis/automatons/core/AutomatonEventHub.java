// $Id: AutomatonEventHub.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 07.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import matvey.utils.RWLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matvey Kazakov
 */
public class AutomatonEventHub implements AutomatonEventTranslator {
	private static boolean DEBUG = false;

    public void sendEvent(AutomatonEvent event) {
        queueEvent(event);
    }

    private final WorkerThread[] workerThreads;

    private List<AutomatonEvent> queue = new ArrayList<AutomatonEvent>();
    private List<AutomatonEventReceiver> receivers = new ArrayList<AutomatonEventReceiver>();
    private Map<AutomatonEventReceiver, RWLock> locks = new HashMap<AutomatonEventReceiver, RWLock>();


    public AutomatonEventHub(int size) {
        workerThreads = new WorkerThread[size];
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new WorkerThread(i);
        }
        start();
    }

    public void addReceiver(AutomatonEventReceiver receiver) {
        synchronized (receivers) {
            receivers.add(receiver);
            synchronized (locks) {
                locks.put(receiver, new RWLock());
            }
        }
    }

    public void removeReceiver(AutomatonEventReceiver receiver) {
        synchronized (receivers) {
            receivers.remove(receiver);
            lockReceiver(receiver);
            synchronized (locks) {
                locks.remove(receiver);
            }
        }
    }

    private boolean lockReceiver(AutomatonEventReceiver receiver) {
        RWLock lock;
        synchronized (locks) {
            lock = locks.get(receiver);
        }
        if (lock == null) {
            return false;
        }
        lock.getWriteLock();
        return true;
    }

    private boolean unlockReceiver(AutomatonEventReceiver receiver) {
        RWLock lock;
        synchronized (locks) {
            lock = locks.get(receiver);
        }
        if (lock == null) {
            return false;
        }
        lock.releaseLock();
        return true;
    }

    private void start() {
        for (int i = 0; i < workerThreads.length; i++) {
            WorkerThread workerThread = workerThreads[i];
            workerThread.start();
        }
    }

    private void queueEvent(AutomatonEvent event) {
        synchronized (queue) {
            queue.add(event);

            if (DEBUG) {
                System.out.println("Queue event is added: " + event + " Queue: " + queue);
            }
            queue.notifyAll();
        }
    }

    private AutomatonEvent extractEvent() {
        synchronized (queue) {
            if (DEBUG) {
                System.out.print("Queue: " + queue);
            }
            AutomatonEvent event = (queue.size() == 0) ? null : queue.remove(0);
            if (DEBUG) {
                System.out.println(" Queue event is extracted: " + event);
            }
            return event;
        }
    }

    private class WorkerThread extends Thread {

        public WorkerThread(int id) {
            super("EventWorkerThread#" + id);
            setDaemon(true);
        }

        public void run() {
            while (!interrupted()) {
                AutomatonEvent event = extractEvent();
                if (event != null) {
                    if (DEBUG) {
                        log(new StringBuilder().append("event \"").append(event).append("\"").toString());
                    }
                    AutomatonEventReceiver[] rcvs = receivers.toArray(new AutomatonEventReceiver[]{});
                    for (int i = 0; i < rcvs.length; i++) {
                        AutomatonEventReceiver rcv = rcvs[i];
//                        if (lockReceiver(rcv)) {
                        synchronized(rcv) {
                            rcv.sendEvent(event);
                            if (DEBUG) {
                                log(new StringBuilder().append("Sent to \"").append(rcv).append("\"").toString());
                            }
//                            unlockReceiver(rcv);
                        }
                    }
                } else {
                    synchronized (queue) {
                        try {
                            if (queue.size() == 0) {
                                queue.wait();
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }

        private void log(String msg) {
            if (DEBUG) {
                System.out.println(new StringBuilder().append("Backbone: ").append(msg));
            }
        }
    }
}