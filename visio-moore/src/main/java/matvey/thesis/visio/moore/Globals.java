/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id: Globals.java,v 1.1 2005/01/04 08:42:55 matvey Exp $
 */
package matvey.thesis.visio.moore;

import java.util.Collection;
import java.util.ArrayList;

/**
 * <code>Globals</code> class
 *
 * @author Matvey Kazakov
 */
public class Globals {

    // Состояние автомата
    public int state, i, j, K, N, sum;
    public int[][] T;
    public int[] M;
    public boolean result;
    public Collection<Integer> positions;

    public Globals(int[] M, int N) {
        K = M.length;
        this.M = M;
        this.N = N;
        positions = new ArrayList<Integer>();
        T = new int[K + 1][N + 1];
        init();
    }

    public void init() {
        state = 0;
        positions.clear();
        for (int ii = 0; ii <= K; ii++)
            for (int jj = 0; jj <= N; jj++) {
                T[ii][jj] = -1;
            }
        i = -1;
        j = -1;
        sum = 0;
        result = false;
    }


}

/*
 * $Log: Globals.java,v $
 * Revision 1.1  2005/01/04 08:42:55  matvey
 * Just added
 *
 */