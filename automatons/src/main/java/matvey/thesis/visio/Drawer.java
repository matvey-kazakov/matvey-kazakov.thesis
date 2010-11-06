// $Id: Drawer.java,v 1.1 2005/09/10 11:31:41 matvey Exp $
/**
 * Date: 31.07.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.visio;

/**
 * Drawer performs static pictures andtext comments rendering/painting.
 *
 * @author Matvey Kazakov
 */
public interface Drawer {

    /**
     * Draws picture and text comment for given state
     * @param s automaton state that picture/text should be created
     */
    void draw(int s);
}

/*
 * $Log: Drawer.java,v $
 * Revision 1.1  2005/09/10 11:31:41  matvey
 * After moving to new library
 *
 */