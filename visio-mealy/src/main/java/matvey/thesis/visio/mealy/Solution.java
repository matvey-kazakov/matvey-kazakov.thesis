/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.mealy;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Решение задачи.
 *
 * @author Matvey Kazakov
 */
public final class Solution {
    /**
     * @param N суммарный вес предметов в рюкзаке
     * @param M массив весов предметов
     * @return список номеров предметов, сумма весов которых равна N,
     *         либо null если это невозможно.
     */
    private static Integer[] solve(final int N, final int[] M) {
        // Переменные циклов
        int i, j;
        // Количество предметов
        int K = M.length;
        // Массив T
        int[][] T = new int[K + 1][N + 1];
        // Результирующие номера
        Collection<Integer> positions = new ArrayList<Integer>();
        // Результат работы true, если суммарный вес можно получить
        boolean result = false;

        // Определение начальных значений функции T
        for (j = 1; j <= N; j++) {
            T[0][j] = 0;
        }
        for (i = 0; i <= K; i++) {
            T[i][0] = 1;
        }

        // Построение таблицы значений функции T
        for (i = 1; i <= K; i++) {
            for (j = 1; j <= N; j++) {
                if (j >= M[i - 1]) {
                    T[i][j] = Math.max(T[i - 1][j], T[i - 1][j - M[i - 1]]);
                } else {
                    T[i][j] = T[i - 1][j];
                }
            }
        }

        // Определение набора предметов
        j = N;
        if (T[K][N] == 1) {
            // Решение существует
            for (i = K; i >= 1; i--) {
                if (T[i - 1][j] == 0) {
                    positions.add(i);
                    j -= M[i - 1];
                }
            }
            // Решение найдено
            result = true;
        }

        // Если решение найдено, то оно возвращается, иначе возвращается null
        return (Integer[]) (result ? positions.toArray(new Integer[0]) : null);
    }

    public static void main(final String[] args) {
        int[] M = new int[]{4, 5, 3, 7, 6};
        int N = 16;
        Integer[] result = solve(N, M);
        System.out.print("[");
        for (int i = 0; i < result.length; i++) {
            Integer integer = result[i];
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(M[integer.intValue() - 1]);
        }
        System.out.println("]");
    }
}

/*
 * $Log$
 */