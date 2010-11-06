// $Id: AutomatonIdFactory.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 07.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * @author Matvey Kazakov
 */
public abstract class AutomatonIdFactory {
    public abstract long getId(String sid);
    public abstract String getSID(long id);
    
    private static AutomatonIdFactory instance;

    public static AutomatonIdFactory getInstance() {
        if (instance == null) {
            instance = DefaultAutomatonIdFactory.getInstance();
        }
        return instance;
    }

    public static void setInstance(AutomatonIdFactory instance) {
        AutomatonIdFactory.instance = instance;
    }
}

/*
 * $Log: AutomatonIdFactory.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */