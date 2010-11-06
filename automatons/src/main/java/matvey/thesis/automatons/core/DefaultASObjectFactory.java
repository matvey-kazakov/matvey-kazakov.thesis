// $Id: DefaultASObjectFactory.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 09.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import matvey.thesis.automatons.core.local.LocalASObjectFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matvey Kazakov
 */
public class DefaultASObjectFactory implements ASObjectFactory {
    private LocalASObjectFactory localFactory;
    private List<ASObjectFactory> delegates = new ArrayList<ASObjectFactory>();

    private static final byte[] DEFAULT_ADDRESS = new byte[]{0, 0, 0, 0};

    private static final DefaultASObjectFactory instance = new DefaultASObjectFactory();

    private DefaultASObjectFactory() {
        byte[] address;
        try {
            address = InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e) {
            address = DEFAULT_ADDRESS;
        }
        long prefix = 0;
        for (int i = 0; i < address.length; i++) {
            prefix <<= 8;
            prefix |= address[i];
        }
        prefix <<= 32;
        localFactory = new LocalASObjectFactory(prefix);
        addFactory(localFactory);
    }

    public long registerObject(ASObjectType type, String sid) {
        return localFactory.registerObject(type, sid);
    }

    public void unregisterObject(ASObjectType type, String sid) {
        localFactory.unregisterObject(type, sid);
    }

    public long getObject(ASObjectType type, String sid) {
        for (ASObjectFactory factory : delegates) {
            try {
                return factory.getObject(type, sid);
            } catch (ASObjectNotFoundException e) {
            }
        }
        return registerObject(type, sid);
    }

    public String getObjectSID(ASObjectType type, long id) {
        for (ASObjectFactory factory : delegates) {
            String sid = factory.getObjectSID(type, id);
            if (sid != null) {
                return sid;
            }
        }
        return null;
    }

    public void addFactory(ASObjectFactory factory) {
        delegates.add(factory);
    }

    public void removeFactory(ASObjectFactory factory) {
        delegates.remove(factory);
    }

    public static DefaultASObjectFactory getInstance() {
        return instance;
    }

}

/*
 * $Log: DefaultASObjectFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */