/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id: Solution.java,v 1.1 2005/01/04 08:42:55 matvey Exp $
 */
package matvey.thesis.visio.multi;


/**
 * Решение задачи.
 *
 * @author Matvey Kazakov
 */
public class SolutionA {
    /**
     * @param N суммарный вес предметов в рюкзаке
     * @param M массив весов предметов
     * @return список номеров предметов, сумма весов которых равна N,
     *         либо null если это невозможно.
     */
    private static Integer[] solve(int N, int[] M) {
    	Globals g = new Globals(M, N);
    	logStateHeaders();
    	while (!g.aM.stopped()) {
    		g.aM.step();
        	logStates(g);
    	}

        // Если решение найдено, то оно возвращается, иначе возвращается null
        return (Integer[])(g.result ? g.positions.toArray(new Integer[0]) : null);
    }

	public static void logStateHeaders() {
		System.out.println("aM\taFR\taFC\taT\taTR\taR");
	}

	public static void logStates(Globals g) {
		System.out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s",
				state(g.aM), state(g.aFR), state(g.aFC), state(g.aT), state(g.aTR), state(g.aR)));
	}

	private static String state(Automaton a) {
		return a.stopped() ? String.valueOf(a.state())+"F" : String.valueOf(a.state());
	}

    public static void main(String[] args) {
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
 * $Log: Solution.java,v $
 * Revision 1.1  2005/01/04 08:42:55  matvey
 * Just added
 *
 */