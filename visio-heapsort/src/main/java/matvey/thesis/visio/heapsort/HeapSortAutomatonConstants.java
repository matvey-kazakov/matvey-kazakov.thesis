// $Id: HeapSortAutomatonConstants.java,v 1.1 2006/02/18 06:37:45 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

/**
 * @author Matvey Kazakov
 */
public interface HeapSortAutomatonConstants {

    String EVT_HEAPSORT_NEXTSTEP = "HeapSort.NextStep";
    String EVT_HEAPSORT_FINISH = "HeapSort.FINISH";

    public static final String VISIO_AUTOMATON_ID = "Visio (A0)";

    public static final int Y0_START = 0;
    public static final int Y1_INIT = 1;
    public static final int Y2_NEXT = 2;
    public static final int Y3_NEXT = 3;
    public static final int Y4_TRANSFER = 4;
    public static final int _Y4_TRANSFER_START = 40;
    public static final int Y4A_TRANSFER_ANIMATION = 41;
    public static final int Y5_ORDER = 5;
    public static final int Y6_MAX = 6;
    public static final int _Y7_SWAP_START = 70;
    public static final int Y7_SWAP = 7;
    public static final int Y7A_SWAP_ANIMATION = 71;
    public static final int Y8_FINISH = 8;

}

/*
 * $Log: HeapSortAutomatonConstants.java,v $
 * Revision 1.1  2006/02/18 06:37:45  matvey
 * HeapSort is committed
 *
 */