// $Id: AutomatonEvent.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 07.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import java.io.Serializable;

/**
 * @author Matvey Kazakov
 */
public class AutomatonEvent implements Serializable{
    public final long source;
    public final long dest;
    public final long event;

    /**
     * Special id to distinguish messages. It should not be unique.
     * The idea is to have enough ids to have possibility to prevent loops.
     * 
     * Loops are identified as messages with the same id from same source coming through filter twice. 
     */ 
 
    public final int id;
    public static final int MAX_ID = 1000000;
    public static volatile int lastId = 0;

    public AutomatonEvent(long source, long dest, long event) {
        this.source = source;
        this.dest = dest;
        this.event = event;
        int newId = lastId++;
        lastId %= MAX_ID;
        id = newId;
    }

    public String toString() {        
        return "from " + AutomatonIdFactory.getInstance().getSID(source) 
                + " to " + AutomatonIdFactory.getInstance().getSID(dest) + ": " 
                + DefaultASObjectFactory.getInstance().getObjectSID(ASObjectType.AS_EVENT, event);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AutomatonEvent event1 = (AutomatonEvent) o;

        if (dest != event1.dest) return false;
        if (event != event1.event) return false;
        if (source != event1.source) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (int) (source ^ (source >>> 32));
        result = 29 * result + (int) (dest ^ (dest >>> 32));
        result = 29 * result + (int) (event ^ (event >>> 32));
        return result;
    }
}

/*
 * $Log: AutomatonEvent.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */