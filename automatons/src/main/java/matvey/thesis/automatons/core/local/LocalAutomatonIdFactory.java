// $Id: LocalAutomatonIdFactory.java,v 1.1 2006/02/18 07:00:32 matvey Exp $
/**
 * Date: 13.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core.local;

import matvey.thesis.automatons.core.AutomatonIdFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Matvey Kazakov
 */
public class LocalAutomatonIdFactory extends AutomatonIdFactory{

    private long lastId;
    private Object mutex = new Object();
    private Map<Long,String> names = new HashMap<Long, String>();
    private Map<String,Long> ids = new HashMap<String, Long>();

    public LocalAutomatonIdFactory(long lastId) {
        this.lastId = lastId;
    }
    

    public long getId(String sid) {
        synchronized(mutex) {
            if (ids.containsKey(sid)) {
                return ids.get(sid).longValue();
            }
            long id = ++lastId;
            Long theId = new Long(id);
            names.put(theId, sid);
            ids.put(sid, theId);
            return id;            
        }
    }

    public String getSID(long id) {
        synchronized(mutex) {
            String name = names.get(new Long(id));
            return name == null ? "" + id : name;
        }
    }

}

/*
 * $Log: LocalAutomatonIdFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:32  matvey
 * just added
 *
 */