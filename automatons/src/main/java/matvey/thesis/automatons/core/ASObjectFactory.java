// $Id: ASObjectFactory.java,v 1.1 2006/02/18 07:00:32 matvey Exp $
/**
 * Date: 09.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * @author Matvey Kazakov
 */
public interface ASObjectFactory {
    long registerObject(ASObjectType type, String sid);
    void unregisterObject(ASObjectType type, String sid);
    long getObject(ASObjectType type, String sid) throws ASObjectNotFoundException;
    String getObjectSID(ASObjectType type, long id);
}

/*
 * $Log: ASObjectFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:32  matvey
 * just added
 *
 */