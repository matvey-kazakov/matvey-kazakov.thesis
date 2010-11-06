// $Id: AutomatonEventSupplier.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 07.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

/**
 * @author Matvey Kazakov
 */
public interface AutomatonEventSupplier {
    void addReceiver(AutomatonEventReceiver receiver);
    void removeReceiver(AutomatonEventReceiver receiver);
}

/*
 * $Log: AutomatonEventSupplier.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */