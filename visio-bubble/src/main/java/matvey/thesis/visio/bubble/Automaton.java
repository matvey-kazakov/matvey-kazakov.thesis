/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.bubble;

import java.util.Arrays;

/**
 * Реализует логику визуализатора.
 *
 * @author Matvey Kazakov
 */
public class Automaton {

    public static final int STATE_START  = 0;
    public static final int STATE_INIT1  = 1;
    public static final int STATE_INIT2  = 2;
    public static final int STATE_SWAP   = 3;
    public static final int STATE_EMERGE = 4;
    public static final int STATE_NEXT   = 5;
    public static final int STATE_FINISH = 6;

    private Globals g;

    public Automaton(Globals g) {
        this.g = g;
    }

    private void makeAutomationStep() {
        switch (g.state) {
            case STATE_START:       // Начальное состояние
                goToAndPerformAction(STATE_INIT1);
                break;

            case STATE_INIT1:
                if (g.i < g.n) {
                    goToAndPerformAction(STATE_INIT2);
                } else {
                    goToAndPerformAction(STATE_START);
                }
                break;

            case STATE_INIT2:
                if (g.j < g.n - g.i + 1 && g.a[g.j-1] > g.a [g.j]) {
                    goToAndPerformAction(STATE_SWAP);
                } else if (g.j < g.n - g.i + 1 && g.a[g.j-1] <= g.a [g.j]) {
                    goToAndPerformAction(STATE_EMERGE);
                } else {
                    goToAndPerformAction(STATE_NEXT);
                }
                break;

            case STATE_SWAP:
                goToAndPerformAction(STATE_EMERGE);
                break;

            case STATE_EMERGE:
                if (g.j < g.n - g.i + 1 && g.a[g.j-1] > g.a [g.j]) {
                    goToAndPerformAction(STATE_SWAP);
                } else if (g.j < g.n - g.i + 1 && g.a[g.j-1] <= g.a [g.j]) {
                    goToAndPerformAction(STATE_EMERGE);
                } else {
                    goToAndPerformAction(STATE_NEXT);
                }
                break;

            case STATE_NEXT:
                if (g.i < g.n) {
                    goToAndPerformAction(STATE_INIT2);
                } else {
                    goToAndPerformAction(STATE_FINISH);
                }
                break;

        }
    }
    private void goToAndPerformAction(int newstate) {
        g.state = newstate;
        switch (newstate) {
            case STATE_START: 	// Начальное состояние
                break;

            case STATE_INIT1:
                g.i = 1;
                break;

            case STATE_INIT2:
                g.j = 1;
                break;

            case STATE_SWAP:
                swap(g.j, g.j - 1);
                break;

            case STATE_EMERGE:
                g.j++;
                break;

            case STATE_NEXT:
                g.i++;
                break;

        }
    }

    /**
     * Один шаг визуализатора состоит из нескольких переходов автомата.
     */
    public void makeStep() {
        int[] badStates = new int[]{STATE_INIT1, STATE_NEXT};
        makeAutomationStep();
        while (!isFinished()
                && Arrays.binarySearch(badStates, g.state) >= 0) {
            makeAutomationStep();
        }
    }

    /**
     * Проверяет, что автомат уже остановился.
     */
    public boolean isFinished() {
        return g.state == STATE_FINISH;
    }


    private void swap(int i, int j) {
        int t = g.a[i];
        g.a[i] = g.a[j];
        g.a[j] = t;
    }


}

/*
 * $Log$
 */