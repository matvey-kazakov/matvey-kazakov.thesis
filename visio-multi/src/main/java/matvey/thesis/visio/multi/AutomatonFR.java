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
public class AutomatonFR extends Automaton {

	public AutomatonFR(Globals g) {
		super(g, new int[] { 0, 1, 3 }, new int[] { 3 }, 1);
	}

	@Override
	protected void makeAutomationStep() {
		switch (state()) {
		case 0: // Начальное состояние
			goToAndPerformAction(1);
			break;

		case 1:
			if (g.j <= g.N) {
				goToAndPerformAction(2);
			} else {
				goToAndPerformAction(3);
			}
			break;

		case 2: // Заполнение
			if (g.j <= g.N) {
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
			g.j = 1;
			break;

		case 2: // Заполнение
			g.T[0][g.j] = 0;
			g.j++;
			break;
		}
	}

}
