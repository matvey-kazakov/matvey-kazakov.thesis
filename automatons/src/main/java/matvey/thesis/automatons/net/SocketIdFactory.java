// $Id: SocketIdFactory.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 28.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.net;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import matvey.thesis.automatons.core.*;

/**
 * Connects to ID allocation server toreceive ids.
 *
 * @author Matvey Kazakov
 */
public class SocketIdFactory extends AutomatonIdFactory implements ASObjectFactory {
    private InetAddress host;

    public SocketIdFactory(String hostName) throws UnknownHostException {
        host = InetAddress.getByName(hostName);
    }

    private Map<String, Long> idCache = new HashMap<String, Long>();
    private Map<Long, String> sidCache = new HashMap<Long, String>();

    public long getId(String sid) {
        Long result = null;
        synchronized(idCache) {
            result = idCache.get(sid);
        }
        if (result == null) {
            result = (Long) performAction(sid);
            synchronized(idCache) {
                idCache.put(sid, result);
            }
        }
        return result.longValue();
    }

    private Object performAction(Object sid) {
        Socket socket;
        try {
            socket = new Socket(host, IdServer.ID_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
        Object o = null;
        if (socket != null) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                new ObjectOutputStream(outputStream).writeObject(sid);
                InputStream inputStream = socket.getInputStream();
                o = new ObjectInputStream(inputStream).readObject();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    public String getSID(long id) {
        Long theId = new Long(id);
        String result = null;
        synchronized(sidCache) {
            result = sidCache.get(theId);
        }
        if (result == null) {
            result = (String) performAction(theId);
            synchronized(sidCache) {
                sidCache.put(theId, result);
            }
        }
        return result;
    }

    public long registerObject(ASObjectType type, String sid) {
        // not supported
        return -1;
    }

    public void unregisterObject(ASObjectType type, String sid) {
        // not supported
    }

    public long getObject(ASObjectType type, String sid) throws ASObjectNotFoundException {
        return getId("EVENT." + sid);
    }

    public String getObjectSID(ASObjectType type, long id) {
        return getSID(id);
    }
}

/*
 * $Log: SocketIdFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */