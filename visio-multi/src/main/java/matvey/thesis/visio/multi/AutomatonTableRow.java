/*
 * Date: Oct 22, 2010
 * Copyright (c) 2001-2010 Flextronics Corp. All Rights Reserved.
 */
package matvey.thesis.visio.multi;


/**
 * <code>AutomatonFR</code>
 *
 * @author Matvey Kazakov
 */
public class AutomatonTableRow extends Automaton {

	public AutomatonTableRow(Globals g) {
		super(g, new int[] { 0, 1, 4, 5 }, new int[] { 5 }, 1);
	}

	@Override
	protected void makeAutomationStep() {
		switch (state()) {
		case 0: // Начальное состояние
			goToAndPerformAction(1);
			break;

		case 1:
			if (g.j > g.N) {
				goToAndPerformAction(5);
			} else if (g.j >= g.M[g.i-1]) {
				goToAndPerformAction(3);
			} else {
				goToAndPerformAction(2);
			}
			break;

		case 2: // Перенос
			goToAndPerformAction(4);
			break;

		case 3: // Альтернатива
			goToAndPerformAction(4);
			break;

		case 4:
			if (g.j > g.N) {
				goToAndPerformAction(5);
			} else if (g.j >= g.M[g.i-1]) {
				goToAndPerformAction(3);
			} else {
				goToAndPerformAction(2);
			}
			break;
		}
	}

	@Override
	protected void performAction() {
		switch (state()) {
		case 0: // Начальное состояние
			break;

		case 1:
			g.j = 1;
			break;

		case 2: // Перенос
			g.T[g.i][g.j] = g.T[g.i-1][g.j];
			break;

		case 3: // Альтернатива
			g.T[g.i][g.j] = Math.max(g.T[g.i-1][g.j], g.T[g.i-1][g.j - g.M[g.i-1]]);
			break;

		case 4: // Переход
			g.j++;
			break;
		}
	}

}
