/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.mealy;

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
            case 0:     // Начальное состояние
                g.j = 1;
                g.i = 0;
                g.state = 1;
                break;

            case 1: // Заполнение 0-й строки
                if (g.j <= g.N) {
                    g.T[0][g.j] = 0;
                    g.j++;
                } else {
                    g.j = 0;
                    g.state = 2;
                }
                break;

            case 2: // Заполнение 0-го столбца
                if (g.i <= g.K) {
                    g.T[g.i][0] = 1;
                    g.i++;
                } else {
                    g.i = 1;
                    g.state = 3;
                }
                break;

            case 3: // Переход к следующей строке
                if (g.i <= g.K) {
                    g.j = 1;
                    g.state = 4;
                } else {
                    g.sum = g.N;
                    g.state = 5;
                }
                break;

            case 4: // Заполнение очередной ячейки
                if (g.j <= g.N && g.j >= g.M[g.i - 1]) {
                    g.T[g.i][g.j] = Math.max(g.T[g.i - 1][g.j], g.T[g.i - 1][g.j - g.M[g.i - 1]]);
                    g.j++;
                } else if (g.j <= g.N && g.j < g.M[g.i - 1]) {
                    g.T[g.i][g.j] = g.T[g.i - 1][g.j];
                    g.j++;
                } else {
                    g.i++;
                    g.state = 3;
                }
                break;

            case 5: // Конец заполнения таблицы
                if (g.T[g.K][g.sum] == 1) { // Решение найдено
                    g.i = g.K;
                    g.state = 6;    // Делать обратный ход
                } else {
                    g.result = false;
                    g.state = 9;    // Обратный ход не требуется
                }
                break;

            case 6: // Обратный ход
                if (g.i > 0) {
                    g.state = 7;
                } else {
                    g.result = true;
                    g.state = 10;
                }
                break;

            case 7: // Поиск очередного предмета
                if (g.T[g.i][g.sum] != g.T[g.i - 1][g.sum]) {
                    g.positions.add(g.i - 1);
                    g.sum -= g.M[g.i - 1];
                    g.state = 8;
                } else {
                    g.state = 8;
                }
                break;

            case 8:
                g.i--;
                g.state = 6;
                break;
        }
    }

    /**
     * Пропуск невизуализируемых переходов.
     */
    public void makeStep() {
        makeAutomationStep();
        while (!isFinished() && (
                g.state == 1 && g.j == g.N + 1
                || g.state == 2 && g.i == g.K + 1
                || g.state == 3
                || g.state == 6
                || g.state == 4 && g.j == g.N + 1
                )) {
            makeAutomationStep();
        }
    }

    /**
     * Проверяет, что автомат уже остановился.
     */
    public boolean isFinished() {
        return g.state == 9 || g.state == 10;
    }


}

/*
 * $Log$
 */