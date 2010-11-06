/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id: Solution.java,v 1.1 2005/01/04 08:42:55 matvey Exp $
 */
package matvey.thesis.visio.multi;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Решение задачи.
 *
 * @author Matvey Kazakov
 */
public class SolutionMulti {
	final int N;
	final int[] M;
    // Массив T
	final int[][] T;
    // Количество предметов
    final int K;

    public SolutionMulti(int N, int[] M) {
		this.N = N;
		this.M = M;
        K = M.length;
        T = new int[K + 1][N + 1];
	}

	/**
     * @param N суммарный вес предметов в рюкзаке
     * @param M массив весов предметов
     * @return список номеров предметов, сумма весов которых равна N,
     *         либо null если это невозможно.
     */
    private Integer[] solve() {
        // Результирующие номера
        Collection<Integer> positions = new ArrayList<Integer>();
        // Результат работы true, если суммарный вес можно получить
        boolean result;

        // Определение начальных значений функции T
        firstRow();
        firstColumn();

        // Построение таблицы значений функции T
        for (int i = 1; i <= K; i++) {
		    tableRow(i);
		}

        // Опредениение набора предметов
        if (T[K][N] == 1) {
            // Решение существует
            searchResult(positions);
            // Решение найдено
            result = true;
        } else {
        	result = false;
        }

        // Если решение найдено, то оно возвращается, иначе возвращается null
        return (Integer[])(result ? positions.toArray(new Integer[0]) : null);
    }

	private void firstRow() {
		for (int j = 1; j <= N; j++) {
            T[0][j] = 0;
        }
	}

	private void firstColumn() {
		for (int i = 0; i <= K; i++) {
            T[i][0] = 1;
        }
	}

	private void tableRow(int i) {
		for (int j = 1; j <= N; j++) {
		    if (j >= M[i - 1]) {
		        T[i][j] = Math.max(T[i - 1][j], T[i - 1][j - M[i - 1]]);
		    } else {
		        T[i][j] = T[i - 1][j];
		    }
		}
	}

	private void searchResult(Collection<Integer> positions) {
		int sum = N;
		for (int i = K; i >= 1; i--) {
		    if (T[i][sum] != T[i - 1][sum]) {
		        positions.add(i);
		        sum -= M[i - 1];
		    }
		}
	}

    public static void main(String[] args) {
        int[] M = new int[]{4, 5, 3, 7, 6};
        int N = 16;
        Integer[] result = new SolutionMulti(N, M).solve();
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
 * $Log: Solution.java,v $
 * Revision 1.1  2005/01/04 08:42:55  matvey
 * Just added
 *
 */