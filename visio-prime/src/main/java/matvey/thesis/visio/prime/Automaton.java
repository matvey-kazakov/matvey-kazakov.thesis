/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.prime;

import java.util.Arrays;

/**
 * Реализует логику визуализатора.
 *
 * @author Matvey Kazakov
 */
public class Automaton {

    public static final int STATE_START  = 0;
    public static final int STATE_NEXT  = 1;
    public static final int STATE_FILL  = 2;
    public static final int STATE_END   = 3;

    private Globals g;

    public Automaton(Globals g) {
        this.g = g;
        goToAndPerformAction(STATE_START);
    }

    private void makeAutomationStep() {
        switch (g.state) {
            case STATE_START:       // Старт
                goToAndPerformAction(STATE_NEXT);
                break;

            case STATE_NEXT:
            	if (g.i > g.q){
                    goToAndPerformAction(STATE_END);
                } else if (!g.a[g.i]) {
                    goToAndPerformAction(STATE_NEXT);
                } else {
                	g.j = g.i*g.i;
                	goToAndPerformAction(STATE_FILL);
                }
                break;

            case STATE_FILL:
                if (g.j <= g.n - g.i) {
                	g.j += g.i;
                    goToAndPerformAction(STATE_FILL);
                } else {
                    goToAndPerformAction(STATE_NEXT);
                }
                break;

        }
    }
    private void goToAndPerformAction(int newstate) {
        g.state = newstate;
        switch (newstate) {
            case STATE_START: 	// Начальное состояние
            	g.i = 1;
            	g.q = (int) Math.sqrt(g.n);
                break;

            case STATE_NEXT:
                g.i++;
                break;

            case STATE_FILL:
                g.a[g.j] = false;
                break;
        }
    }

    /**
     * Один шаг визуализатора состоит из нескольких переходов автомата.
     */
    public void makeStep() {
        makeAutomationStep();
    }

    /**
     * Проверяет, что автомат уже остановился.
     */
    public boolean isFinished() {
        return g.state == STATE_END;
    }

	public void init() {
        goToAndPerformAction(STATE_START);
	}

}

/*
 * $Log$
 */