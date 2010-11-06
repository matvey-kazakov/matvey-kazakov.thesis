// $Id: Globals.java,v 1.1 2006/02/18 06:37:44 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

/**
 * Contains global variables for heap-sort algorithm.
 *
 * @author Matvey Kazakov
 */
class Globals {
    // unsorted array size (used on the first stage)
    public int usize;
    // heap size
    public int hsize;
    // current element
    public int c;
    // previous current element
    public int old_c;
    // maximum elelement among x and its children
    public int cmax;
    // sorted array of elements
    public int[] a;

    // flag indicates animation feature
    public boolean animationEnabled;
    // parameter contains number of slides in one animation
    public int animationNsteps;

    /**
     * Constructs Globals with default initial data.
     */
    public Globals() {
        init();
    }

    /**
     * Initializes Globals with default initial data.
     */
    public void init() {
        usize = 0;
        hsize = 0;
        c = 0;
        old_c = 0;
        cmax = 0;
        animationNsteps = 20;
        // default array is used just for demo purposes
        a = new int[]{4, 1, 23, 3, 2, 16, 20, 9, 10, 21, 14, 8, 7, 15, 11};
    }

    /**
     * Evaluates left child node index in the array
     * @param c index of the node whose child is searched
     * @return child index
     */
    public static int left(int c) {
        return c * 2 + 1;
    }

    /**
     * Evaluates right child node index in the array
     * @param c index of the node whose child is searched
     * @return child index
     */
    public static int right(int c) {
        return c * 2 + 2;
    }
}

/*
 * $Log: Globals.java,v $
 * Revision 1.1  2006/02/18 06:37:44  matvey
 * HeapSort is committed
 *
 */