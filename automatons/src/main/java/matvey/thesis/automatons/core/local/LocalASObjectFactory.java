// $Id: LocalASObjectFactory.java,v 1.1 2006/02/18 07:00:32 matvey Exp $
/**
 * Date: 09.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core.local;

import matvey.thesis.automatons.core.ASObjectFactory;
import matvey.thesis.automatons.core.ASObjectNotFoundException;
import matvey.thesis.automatons.core.ASObjectType;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Matvey Kazakov
 */
public class LocalASObjectFactory implements ASObjectFactory{
    private Map <ASObjectType, Map<String, Long>> registry = new HashMap<ASObjectType, Map<String, Long>>();
    private Map <ASObjectType, Map<Long, String>> backregistry = new HashMap<ASObjectType, Map<Long, String>>();
    private final long idPrefix;
    private long lastId = 0; 
    private Object mutex = new Object();

    public LocalASObjectFactory(long idPrefix) {
        this.idPrefix = idPrefix;
    }

    public long registerObject(ASObjectType type, String sid) {
        long value;
        synchronized(mutex) {
            value = idPrefix + (lastId++);
        }
        getObjectsByType(type).put(sid, value);
        getBackObjectsByType(type).put(value, sid);
        return value;
    }

    public void unregisterObject(ASObjectType type, String sid) {
        getBackObjectsByType(type).remove(getObjectsByType(type).remove(sid));
    }

    public long getObject(ASObjectType type, String sid) {
        Long aLong = getObjectsByType(type).get(sid);
        if (aLong == null) {
            throw new ASObjectNotFoundException();
        }
        return aLong.longValue();
    }

    public String getObjectSID(ASObjectType type, long id) {
        return getBackObjectsByType(type).get(id);
    }

    private Map<String, Long> getObjectsByType(ASObjectType type) {
        Map<String, Long> map = registry.get(type);
        if (map == null) {
            map = new HashMap<String, Long>();
            registry.put(type, map);
        }
        return map;
    }
    
    private Map<Long, String> getBackObjectsByType(ASObjectType type) {
        Map<Long, String> map = backregistry.get(type);
        if (map == null) {
            map = new HashMap<Long, String>();
            backregistry.put(type, map);
        }
        return map;
    }
    
}

/*
 * $Log: LocalASObjectFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:32  matvey
 * just added
 *
 */