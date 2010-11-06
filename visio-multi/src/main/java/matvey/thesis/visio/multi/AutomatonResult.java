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
public class AutomatonResult extends Automaton {

	public AutomatonResult(Globals g) {
		super(g, new int[] { 5 }, new int[] { 5 }, 2);
	}

	@Override
	protected void makeAutomationStep() {
		switch (state()) {
		case 0: // Начальное состояние
			goToAndPerformAction(1);
			break;

		case 1:
			if (g.i < 1) {
				goToAndPerformAction(5);
			} else if (g.T[g.i][g.sum] != g.T[g.i-1][g.sum]) {
				goToAndPerformAction(2);
			} else {
				goToAndPerformAction(3);
			}
			break;

		case 2: // Добавление
			goToAndPerformAction(4);
			break;

		case 3: // Новая ячейка
			goToAndPerformAction(4);
			break;

		case 4:
			if (g.i < 1) {
				goToAndPerformAction(5);
			} else if (g.T[g.i][g.sum] != g.T[g.i-1][g.sum]) {
				goToAndPerformAction(2);
			} else {
				goToAndPerformAction(3);
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
			g.sum = g.N;
			g.i = g.K;
			break;

		case 2: // Добавление
			g.positions.add(g.i);
			g.sum -= g.M[g.i-1];
			break;

		case 3: // Новая ячейка
			break;

		case 4: // Переход
			g.i--;
			break;
		}
	}

}
