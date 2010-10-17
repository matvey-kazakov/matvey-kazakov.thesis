/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id: Automaton.java,v 1.1 2005/01/04 08:42:54 matvey Exp $
 */
package matvey.thesis.visio.moore;

import java.util.Arrays;

/**
 * Реализует логику визуализатора.
 *
 * @author Matvey Kazakov
 */
public class Automaton {

    private Globals g;

    public Automaton(Globals g) {
        this.g = g;
    }

    private void makeAutomationStep() {
        switch (g.state) {
            case 0: 	// Начальное состояние
                goToAndPerformAction(1);
                break;

            case 1:
                if (g.j <= g.N) {
                    goToAndPerformAction(2);
                } else {
                    goToAndPerformAction(3);
                }
                break;

            case 2:	// Заполнение 0-й строки
                if (g.j <= g.N) {
                    goToAndPerformAction(2);
                } else {
                    goToAndPerformAction(3);
                }
                break;

            case 3:
                if (g.i <= g.K) {
                    goToAndPerformAction(4);
                } else {
                    goToAndPerformAction(5);
                }
                break;

            case 4:	// Заполнение 0-го столбца
                if (g.i <= g.K) {
                    goToAndPerformAction(4);
                } else {
                    goToAndPerformAction(5);
                }
                break;

            case 5:
                if (g.i <= g.K) {
                    goToAndPerformAction(6);
                } else if (g.T[g.K][g.N] == 1) {
                    goToAndPerformAction(12);
                } else {
                    goToAndPerformAction(11);
                }
                break;

            case 6:	// Переход к следующей строке
                if (g.j > g.N) {
                    goToAndPerformAction(7);
                } else if (g.j >= g.M[g.i - 1]) {
                    goToAndPerformAction(9);
                } else {
                    goToAndPerformAction(8);
                }
                break;

            case 7:
                if (g.i <= g.K) {
                    goToAndPerformAction(6);
                } else if (g.T[g.K][g.N] == 1) {
                    goToAndPerformAction(12);
                } else {
                    goToAndPerformAction(11);
                }
                break;

            case 8:	// Перенос
                goToAndPerformAction(10);
                break;

            case 9:	// Альтернатива
                goToAndPerformAction(10);
                break;

            case 10:	// Переход к следующей строке
                if (g.j > g.N) {
                    goToAndPerformAction(7);
                } else if (g.j >= g.M[g.i - 1]) {
                    goToAndPerformAction(9);
                } else {
                    goToAndPerformAction(8);
                }
                break;

            case 12:
                goToAndPerformAction(13);
                break;

            case 13:
                if (g.i == 0) {
                    goToAndPerformAction(14);
                } else if (g.T[g.i][g.sum] == g.T[g.i-1][g.sum]) {
                    goToAndPerformAction(17);
                } else {
                    goToAndPerformAction(16);
                }
                break;

            case 15:
                if (g.i == 0) {
                    goToAndPerformAction(14);
                } else if (g.T[g.i][g.sum] == g.T[g.i-1][g.sum]) {
                    goToAndPerformAction(17);
                } else {
                    goToAndPerformAction(16);
                }
                break;

            case 16: // элемент найден
                goToAndPerformAction(15);
                break;

            case 17:
                goToAndPerformAction(15);
                break;
        }
    }
    private void goToAndPerformAction(int newstate) {
        g.state = newstate;
        switch (newstate) {
            case 0: 	// Начальное состояние
                g.state = 1;
                break;

            case 1:
                g.j = 1;
                break;

            case 2:	// Заполнение 0-й строки
                g.T[0][g.j] = 0;
                g.j++;
                break;

            case 3:
                g.i = 0;
                break;

            case 4:	// Заполнение 0-го столбца
                g.T[g.i][0] = 1;
                g.i++;
                break;

            case 5:
                g.i = 1;
                break;

            case 6:	// Переход к следующей строке
                g.j = 1;
                break;

            case 7:
                g.i++;
                break;

            case 8:	// Перенос
                g.T[g.i][g.j] = g.T[g.i - 1][g.j];
                break;

            case 9:	// Альтернатива
                g.T[g.i][g.j] = Math.max(g.T[g.i - 1][g.j], g.T[g.i - 1][g.j - g.M[g.i - 1]]);
                break;

            case 10:	// Переход к следующей строке
                g.j++;
                break;

            case 12:
                g.sum = g.N;
                break;

            case 13:
                g.i = g.K;
                break;

            case 15:
                g.i--;
                break;

            case 16: // элемент найден
                g.positions.add(new Integer(g.i-1));
                g.sum -= g.M[g.i-1];
                break;
        }
    }

    /**
     * Один шаг визуализатора состоит из нескольких переходов автомата.
     */
    public void makeStep() {
        int[] badStates = new int[]{1, 3, 5, 6, 7, 10};
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
        return g.state == 11 || g.state == 14;
    }


}

/*
 * $Log: Automaton.java,v $
 * Revision 1.1  2005/01/04 08:42:54  matvey
 * Just added
 *
 */