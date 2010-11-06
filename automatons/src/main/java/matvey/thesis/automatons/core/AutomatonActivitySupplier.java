// $Id: AutomatonActivitySupplier.java,v 1.1 2006/02/18 07:00:33 matvey Exp $
/**
 * Date: 06.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.core;

import matvey.thesis.automatons.core.AutomatonActivityListener;

/**
 * @author Matvey Kazakov
 */
public interface AutomatonActivitySupplier {
    void addActivityListener(AutomatonActivityListener listener);
    void removeActivityListener(AutomatonActivityListener listener);
}

/*
 * $Log: AutomatonActivitySupplier.java,v $
 * Revision 1.1  2006/02/18 07:00:33  matvey
 * just added
 *
 */