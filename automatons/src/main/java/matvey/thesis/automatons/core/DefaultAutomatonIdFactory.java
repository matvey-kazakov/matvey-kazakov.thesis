// $Id: DefaultAutomatonIdFactory.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 14.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import matvey.thesis.automatons.core.local.LocalAutomatonIdFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Matvey Kazakov
 */
class DefaultAutomatonIdFactory extends AutomatonIdFactory{
    private LocalAutomatonIdFactory localFactory;

    private static final byte[] DEFAULT_ADDRESS = new byte[]{0,0,0,0};
    private static DefaultAutomatonIdFactory instance = new DefaultAutomatonIdFactory();

    private DefaultAutomatonIdFactory() {
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
        localFactory = new LocalAutomatonIdFactory(prefix+1);
    }

    public long getId(String sid) {
        return localFactory.getId(sid);
    }

    public String getSID(long id) {
        return localFactory.getSID(id);
    }

    public static DefaultAutomatonIdFactory getInstance() {
        return instance;
    }
    
}

/*
 * $Log: DefaultAutomatonIdFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */