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
public class AutomatonTable extends Automaton {

	public AutomatonTable(Globals g) {
		super(g, new int[] { 0, 1, 2, 21, 3, 4 }, new int[] { 4 }, 2);
	}

	@Override
	protected void makeAutomationStep() {
		switch (state()) {
		case 0: // Начальное состояние
			goToAndPerformAction(1);
			break;

		case 1:
			if (g.i <= g.K) {
				goToAndPerformAction(21);
			} else {
				goToAndPerformAction(4);
			}
			break;

		case 21: // Заполнение строки: старт
			if (g.aTR.stopped()) {
				goToAndPerformAction(3);
			} else {
				goToAndPerformAction(2);
			}
			break;

		case 2: // Заполнение строки
			if (g.aTR.stopped()) {
				goToAndPerformAction(3);
			} else {
				goToAndPerformAction(2);
			}
			break;

		case 3:
			if (g.i <= g.K) {
				goToAndPerformAction(21);
			} else {
				goToAndPerformAction(4);
			}
			break;
		}
	}

	@Override
	protected void performAction() {
		switch (state()) {
		case 0:  // Начальное состояние
			break;

		case 1:
			g.i = 1;
			break;

		case 21: // Заполнение строки
			initInner(g.aTR);
			break;

		case 2:  // Заполнение строки
			g.aTR.step();
			break;

		case 3:  // Переход
			g.i++;
			break;
		}
	}

}
