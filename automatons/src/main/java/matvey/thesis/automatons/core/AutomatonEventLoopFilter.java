// $Id: AutomatonEventLoopFilter.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 28.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import matvey.utils.MKLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This simple class prevents event loops.
 * It looks like {@link AutomatonEventTranslator} but when it receives event it checks whether
 * it is sent already and if yes, just kills event without forwarding.
 * <p/>
 * To prevent buffer overflow, it stores timestamps and cleans buffer after timeout.
 *
 * @author Matvey Kazakov
 */
public class AutomatonEventLoopFilter {
    public static final long DEFAULT_TIMEOUT = 1000; // 1 second

    private Set<EventWrapper> buffer = new HashSet<EventWrapper>();
    private List<EventWrapper> timestamps = new ArrayList<EventWrapper>();
    private EventFilter eventFilter1 = new EventFilter();
    private EventFilter eventFilter2 = new EventFilter();

    private long timeout;
    private Logger logger;

    public AutomatonEventLoopFilter(AutomatonEventTranslator translator1, AutomatonEventTranslator translator2) {
        this(translator1, translator2, DEFAULT_TIMEOUT);
    }

    public AutomatonEventLoopFilter(AutomatonEventTranslator translator1, AutomatonEventTranslator translator2, long timeout) {
        translator1.addReceiver(eventFilter1);
        eventFilter1.addReceiver(translator2);
        translator2.addReceiver(eventFilter2);
        eventFilter2.addReceiver(translator1);
        this.timeout = timeout;
        logger = MKLogger.createLogger("Filter", Level.FINE);
    }

    private boolean allowSend(AutomatonEvent event) {
        boolean send = false;
        EventWrapper o = new EventWrapper(event);
        cleanUpBuffer();
        if (!buffer.contains(o)) {
            buffer.add(o);
            timestamps.add(o);
            send = true;
        }
        return send;
    }

    private void cleanUpBuffer() {
        long currentTime = System.currentTimeMillis();
        while (true) {
            if (timestamps.size() == 0) {
                break;
            }
            if (currentTime - timestamps.get(0).getTimestamp() > timeout) {
                buffer.remove(timestamps.remove(0));
            } else {
                break;
            }
        }
    }

    private class EventFilter implements AutomatonEventTranslator {
        private List<AutomatonEventReceiver> receivers = new ArrayList<AutomatonEventReceiver>();

        public void addReceiver(AutomatonEventReceiver receiver) {
            receivers.add(receiver);
        }

        public void removeReceiver(AutomatonEventReceiver receiver) {
            receivers.remove(receiver);
        }

        public void sendEvent(AutomatonEvent event) {
            if (!allowSend(event)) {
                return;
            }
            AutomatonEventReceiver[] array;
            synchronized (receivers) {
                array = receivers.toArray(new AutomatonEventReceiver[receivers.size()]);
            }
            for (AutomatonEventReceiver receiver : array) {
                receiver.sendEvent(event);
                logger.fine("event \"" + event + "\" is sent to " + receiver);
            }
        }
    }

    private class EventWrapper {
        private long source;
        private int id;
        private long timestamp;

        public EventWrapper(AutomatonEvent event) {
            source = event.source;
            id = event.id;
            timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final EventWrapper eventWrapper = (EventWrapper) o;

            if (id != eventWrapper.id) {
                return false;
            }
            if (source != eventWrapper.source) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = (int) (source ^ (source >>> 32));
            result = 29 * result + id;
            return result;
        }
    }

}

/*
 * $Log: AutomatonEventLoopFilter.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */