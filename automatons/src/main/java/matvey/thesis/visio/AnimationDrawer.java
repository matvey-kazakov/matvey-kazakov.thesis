// $Id: AnimationDrawer.java,v 1.1 2005/09/10 11:31:40 matvey Exp $
/**
 * Date: 31.07.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.visio;

/**
 * This interface represents extention for {@link Drawer} with animation support.
 *
 * @author Matvey Kazakov
 */
public interface AnimationDrawer extends Drawer {
    /**
     * Draws picture/text for specific state and animation step.
     *
     * @param s state of the main visualizer automaton
     * @param step animation step.
     *
     * @see #draw(int)
     */
    void draw(int s, int step);
}

/*
 * $Log: AnimationDrawer.java,v $
 * Revision 1.1  2005/09/10 11:31:40  matvey
 * After moving to new library
 *
 */